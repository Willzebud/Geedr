package com.app_will.feedarticlesjetpackcomposeapplication.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_will.geedrapplication.navigation.ScreenNavigation
import com.app_will.geedrapplication.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val myPref: SharedPreferencesManager
) : ViewModel() {

    private val _isProgressBarActiveStateFlow = MutableStateFlow(false)
    val isProgressBarActiveStateFlow = _isProgressBarActiveStateFlow.asStateFlow()

    private val _navigateToLogin = MutableSharedFlow<ScreenNavigation>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    private val _navigateToMain = MutableSharedFlow<ScreenNavigation>()
    val navigateToMain = _navigateToMain.asSharedFlow()

    fun navigate() {
        viewModelScope.launch {
            _isProgressBarActiveStateFlow.value = true
            delay(3000)

            myPref.loadSession()
            val user = myPref.getUser()

            Log.d("TESSSST", "$user")

            if(user != null) _navigateToMain.emit(ScreenNavigation.Main)
            else _navigateToLogin.emit(ScreenNavigation.Login)

            _isProgressBarActiveStateFlow.value = false
        }
    }
}