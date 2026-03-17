package com.app_will.geedrapplication.ui.userscheckin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.data.dto.UserDto
import com.app_will.geedrapplication.data.repository.ApiRepository
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_400
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_404
import com.app_will.geedrapplication.utils.API_RESPONSE_CODE_500
import com.app_will.geedrapplication.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsersCheckInViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()
    private val _isRefreshingStateFlow = MutableStateFlow(false)
    val isRefreshingStateFlow = _isRefreshingStateFlow.asStateFlow()

    private val _userCheckInFilterStateFlow = MutableStateFlow<List<UserDto>>(emptyList())
    val userCheckInFilterStateFlow = _userCheckInFilterStateFlow.asStateFlow()
    private val _userCheckInActive = MutableStateFlow(0)
    val userCheckInActive = _userCheckInActive.asStateFlow()


    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserSharedFlow.asSharedFlow()

    private val _navigateToScreen = MutableSharedFlow<String>()
    val navigateToScreen = _navigateToScreen.asSharedFlow()

    fun getUsers() {
        _isProgressBarActiveStateFlow.value = true
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.getUsers()
                }

                if (res.isSuccessful) {
                    val userCheckInList = res.body().orEmpty()

                    _userCheckInFilterStateFlow.value = userCheckInList.filter { user ->
                        user.isUserCheckin && user.isUserVisible
                    }

                    _userCheckInActive.value = 0
                    _userCheckInActive.value = userCheckInList.filter { user ->
                        user.isUserVisible
                    }.size

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
        }
    }

    fun swipeRefresh() {
        viewModelScope.launch {
            _isRefreshingStateFlow.value = true
            delay(2000)
            getUsers()
            _isRefreshingStateFlow.value = false
            _isProgressBarActiveStateFlow.value = false
        }
    }

    fun navigateToUserCheckInProfileScreen(
        userId: String
    ) {
        viewModelScope.launch {
            _navigateToScreen.emit("${MainNavigation.UserCheckInProfile.route}/$userId")
        }

    }
}