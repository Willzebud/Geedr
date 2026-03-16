package com.app_will.geedrapplication.navigation

sealed class RootNavigation(val route: String) {
    object Splash : RootNavigation("splash")
    object Login : RootNavigation("login")
    object Main: RootNavigation("main")

}


sealed class MainNavigation(val route: String){
    object Places : MainNavigation("places")
    object CheckInUser: MainNavigation("checkInUserScreen")
    object UserCheckInProfile: MainNavigation("userCheckInProfile")
    object Profile: MainNavigation("profile")
    object Messaging: MainNavigation("messaging")
}