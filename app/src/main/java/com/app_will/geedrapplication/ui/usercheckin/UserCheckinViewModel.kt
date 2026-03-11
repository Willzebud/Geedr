package com.app_will.geedrapplication.ui.usercheckin

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.network.dto.UserDto
import com.app_will.geedrapplication.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserCheckinViewModel @Inject constructor(
    private val apiRepository: ApiRepository
): ViewModel() {

    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()
    private val _isRefreshingStateFlow = MutableStateFlow(false)
    val isRefreshingStateFlow = _isRefreshingStateFlow.asStateFlow()

    private val _userCheckinFilterStateFlow = MutableStateFlow<List<UserDto>>(emptyList())
    val userCheckinFilterStateFlow = _userCheckinFilterStateFlow.asStateFlow()
    private val _isUserCheckinStateFlow = MutableStateFlow(false)
    val isUserCheckinStateFlow = _isUserCheckinStateFlow.asStateFlow()

    private val _usersListStateFlow = MutableStateFlow<List<UserDto>>(emptyList())
    val usersListStateFlow = _usersListStateFlow.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers(){

        viewModelScope.launch {
            _isProgressBarActiveStateFlow.value = true

            try {
                val res = withContext(Dispatchers.IO){
                    apiRepository.getUsers()
                }

                if (res.isSuccessful){
                    _usersListStateFlow.value = res.body().orEmpty()

                    val userChekinList = _usersListStateFlow.value

                    _userCheckinFilterStateFlow.value = userChekinList.filter { it.isUserCheckin }

                }

            } catch (e: Exception){

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

    fun userCheckinFilter(isCheckin: Boolean){

    }
}