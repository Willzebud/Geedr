package com.app_will.geedrapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app_will.feedarticlesjetpackcomposeapplication.ui.splash.SplashViewModel
import com.app_will.geedrapplication.ui.login.LoginScreen
import com.app_will.geedrapplication.ui.login.LoginViewModel
import com.app_will.geedrapplication.ui.main.MainScreen
import com.app_will.geedrapplication.ui.main.MainViewModel
import com.app_will.geedrapplication.ui.messaging.MessagingScreen
import com.app_will.geedrapplication.ui.messaging.MessagingViewModel
import com.app_will.geedrapplication.ui.splash.SplashScreen
import com.app_will.geedrapplication.ui.usercheckin.UserCheckinScreen
import com.app_will.geedrapplication.ui.usercheckin.UserCheckinViewModel
import com.app_will.geedrapplication.ui.userprofil.UserProfileScreen
import com.app_will.geedrapplication.ui.userprofil.UserProfileViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenNavigation.Splash.route) {

        composable(ScreenNavigation.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController = navController, splashViewModel = splashViewModel)
        }

        composable(ScreenNavigation.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController = navController, loginViewModel = loginViewModel)
        }

        composable(ScreenNavigation.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(ScreenNavigation.Profile.route) {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            UserProfileScreen(
                navController = navController,
                userProfileViewModel = userProfileViewModel
            )
        }

        composable(ScreenNavigation.Messaging.route) {
            val messagingViewModel: MessagingViewModel = hiltViewModel()
            MessagingScreen(navController = navController, messagingViewModel = messagingViewModel)
        }

        composable(
            ScreenNavigation.CheckinProfile.route +
                "/{placeType}/{placeName}/{addressCity}/{userCheckin}",
            arguments = listOf(
                navArgument("placeType") {
                    type = NavType.StringType
                },
                navArgument("placeName") {
                    type = NavType.StringType
                },
                navArgument("addressCity"){
                    type = NavType.StringType
                },
                navArgument("userCheckin"){
                    type = NavType.IntType
                }
            )
        ) {navBackStackEntry ->
            val placeType = navBackStackEntry.arguments?.getString("placeType") ?: ""
            val placeName = navBackStackEntry.arguments?.getString("placeName") ?: ""
            val addressCity = navBackStackEntry.arguments?.getString("addressCity") ?: ""
            val userCheckin = navBackStackEntry.arguments?.getInt("userCheckin") ?: 0
            val userCheckinViewModel: UserCheckinViewModel = hiltViewModel()
            UserCheckinScreen(
                placeName = placeName,
                placeType = placeType,
                addressCity = addressCity,
                nbruserCheckin = userCheckin,
                navController = navController,
                userCheckinViewModel = userCheckinViewModel
            )
        }
    }
}