package com.app_will.geedrapplication.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.RootNavigation
import com.app_will.geedrapplication.data.repository.ApiRepository
import com.app_will.geedrapplication.data.sharedpreferences.SharedPreferencesManager
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_400
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_404
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_500
import com.app_will.geedrapplication.utils.UiEvent
import com.app_will.geedrapplication.utils.validateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val myPref: SharedPreferencesManager

) : ViewModel() {
    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()
    private val _isEnabledButtonStateFlow = MutableStateFlow(true)
    val isEnabledButtonStateFlow = _isEnabledButtonStateFlow.asStateFlow()

    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserSharedFlow = _responseUserSharedFlow.asSharedFlow()


    private val _navigateToMainSharedFlow = MutableSharedFlow<RootNavigation>()
    val navigateToMainSharedFlow = _navigateToMainSharedFlow.asSharedFlow()

    fun login(
        userIdentifier: String,
        userPassword: String
    ) {
        viewModelScope.launch {
            _isProgressBarActiveStateFlow.value = true
            _isEnabledButtonStateFlow.value = false

            val validation = validateLogin(
                userIdentifier,
                userPassword
            )

            if (!validation.isValid) {
                validation.errorMessage?.let { errorMessage ->
                    _responseUserSharedFlow.emit(
                        UiEvent.ShowToast(errorMessage)
                    )
                }
            }

            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.login(userIdentifier.toLong(), userPassword)
                }
                if (res.isSuccessful) {
                    val user = res.body()?.firstOrNull()
                    val userId = user?.userId

                    if (userId != null) {
                        myPref.createSession(userId)

                        _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.authenticated_successful)
                        )

                        _navigateToMainSharedFlow.emit(RootNavigation.Main)
                    } else {
                        _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.login_screen_wrong_mail_password)
                        )
                    }
                } else {
                    when (res.code()) {
                        API_RESPONSE_CODE_400 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.error_occurred)
                        )

                        API_RESPONSE_CODE_404 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.api_response_404)
                        )

                        API_RESPONSE_CODE_500 -> _responseUserSharedFlow.emit(
                            UiEvent.ShowToast(R.string.api_response_500)
                        )
                    }
                }

            } catch (e: Exception) {
                _responseUserSharedFlow.emit(
                    UiEvent.ShowToast(R.string.error_occurred)
                )
            }

            _isProgressBarActiveStateFlow.value = false
            _isEnabledButtonStateFlow.value = true
        }
    }

}