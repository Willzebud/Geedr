package com.app_will.geedrapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app_will.feedarticlesjetpackcomposeapplication.ui.splash.SplashViewModel
import com.app_will.geedrapplication.ui.auth.login.LoginScreen
import com.app_will.geedrapplication.ui.auth.login.LoginViewModel
import com.app_will.geedrapplication.ui.main.MainScreen
import com.app_will.geedrapplication.ui.splash.SplashScreen

@Composable
fun RootNavigation() {

    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = RootNavigation.Splash.route) {

        composable(RootNavigation.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController = rootNavController, splashViewModel = splashViewModel)
        }

        composable(RootNavigation.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController = rootNavController, loginViewModel = loginViewModel)
        }

        composable(RootNavigation.Main.route) {
            MainScreen()
        }


    }
}