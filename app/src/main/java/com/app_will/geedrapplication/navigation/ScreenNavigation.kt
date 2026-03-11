package com.app_will.geedrapplication.navigation

sealed class ScreenNavigation(val route: String) {
    object Splash : ScreenNavigation("splash")
    object Login : ScreenNavigation("login")
    object Main: ScreenNavigation("main")
    object Profile: ScreenNavigation("profile")
    object Messaging: ScreenNavigation("messaging")
    object CheckinProfile: ScreenNavigation("checkinProfile")
}