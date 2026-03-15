package com.app_will.geedrapplication.ui.userscheckin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.network.dto.UserDto
import com.app_will.geedrapplication.repository.ApiRepository
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
class UsersCheckinViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()
    private val _isRefreshingStateFlow = MutableStateFlow(false)
    val isRefreshingStateFlow = _isRefreshingStateFlow.asStateFlow()

    private val _userCheckinFilterStateFlow = MutableStateFlow<List<UserDto>>(emptyList())
    val userCheckinFilterStateFlow = _userCheckinFilterStateFlow.asStateFlow()
    private val _userCheckinActive = MutableStateFlow(0)
    val userCheckinActive = _userCheckinActive.asStateFlow()


    private val _responseUserSharedFlow = MutableSharedFlow<UiEvent>()
    val responseUserStateFlow = _responseUserSharedFlow.asSharedFlow()

    private val _navigateToScreen = MutableSharedFlow<String>()
    val navigateToScreen = _navigateToScreen.asSharedFlow()

    private val _usersListStateFlow = MutableStateFlow<List<UserDto>>(emptyList())
    val usersListStateFlow = _usersListStateFlow.asStateFlow()

    fun getUsers() {
        _userCheckinActive.value = 0
        _isProgressBarActiveStateFlow.value = true
        viewModelScope.launch {
            try {
                val res = withContext(Dispatchers.IO) {
                    apiRepository.getUsers()
                }

                if (res.isSuccessful) {
                    _usersListStateFlow.value = res.body().orEmpty()

                    val userCheckinList = _usersListStateFlow.value
                    _userCheckinFilterStateFlow.value = userCheckinList.filter { user ->
                        user.isUserCheckin && user.isUserVisible
                    }



                    userCheckinList.forEach { user ->
                        if (user.isUserVisible) {
                            _userCheckinActive.value += 1
                        }
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

    fun navigateToScreen(
        userId: String
    ) {
        viewModelScope.launch {
            _navigateToScreen.emit("${MainNavigation.UserCheckinProfil.route}/$userId")
        }

    }
}