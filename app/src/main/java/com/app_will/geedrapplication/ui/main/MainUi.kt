package com.app_will.geedrapplication.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.navigation.RootNavigation
import com.app_will.geedrapplication.ui.components.BottomNavigationBar
import com.app_will.geedrapplication.ui.messaging.MessagingScreen
import com.app_will.geedrapplication.ui.messaging.MessagingViewModel
import com.app_will.geedrapplication.ui.places.PlacesScreen
import com.app_will.geedrapplication.ui.places.PlacesViewModel
import com.app_will.geedrapplication.ui.userscheckin.UsersCheckInScreen
import com.app_will.geedrapplication.ui.userscheckin.UsersCheckInViewModel
import com.app_will.geedrapplication.ui.usercheckinprofile.CheckInProfileScreen
import com.app_will.geedrapplication.ui.usercheckinprofile.UserCheckInProfileViewModel
import com.app_will.geedrapplication.ui.userprofil.UserProfileScreen
import com.app_will.geedrapplication.ui.userprofil.UserProfileViewModel
import com.app_will.geedrapplication.utils.NAV_ARG_CITY_NAME
import com.app_will.geedrapplication.utils.NAV_ARG_PLACE_NAME
import com.app_will.geedrapplication.utils.NAV_ARG_PLACE_TYPE
import com.app_will.geedrapplication.utils.USER_CHECK_IN_ID
import com.app_will.geedrapplication.utils.USER_CHECK_IN_PROFILE_ROUTE
import com.app_will.geedrapplication.utils.USERS_CHECK_IN_ROUTE

@Composable
fun MainScreen(
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showDialog = remember { mutableStateOf(false) }
    val notificationTrigger = remember { mutableStateOf(false) }

    val isBottomBarVisible = when (currentDestination?.route ) {
        MainNavigation.UserCheckInProfile.route + USER_CHECK_IN_PROFILE_ROUTE -> false
        else -> true
    }

    MainContent(
        isBottomBarVisible = isBottomBarVisible,
        currentDestination = currentDestination,
        mainNavController = mainNavController,
        isDialogOpen = showDialog,
        isProfileLiked = notificationTrigger,
        onOpenDialog = {
            showDialog.value = true
            notificationTrigger.value = true
        },

    )
}

@Composable
fun MainContent(
    isBottomBarVisible: Boolean,
    currentDestination: NavDestination?,
    mainNavController : NavHostController,
    isProfileLiked: MutableState<Boolean>,
    isDialogOpen: MutableState<Boolean>,
    onOpenDialog: () -> Unit

) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = isBottomBarVisible) {
                if (currentDestination != null) {
                    BottomNavigationBar(
                        navController = mainNavController,
                        currentDestination = currentDestination,
                        isLiked = isProfileLiked
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Surface(
                onClick = onOpenDialog,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(20.dp)
                    .width(20.dp)
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {

            }

            NavHost(
                navController = mainNavController,
                startDestination = RootNavigation.Main.route
            ) {
                navigation(
                    route = RootNavigation.Main.route,
                    startDestination = MainNavigation.Places.route
                ) {
                    composable(MainNavigation.Places.route) {
                        val placesViewModel: PlacesViewModel = hiltViewModel()
                        PlacesScreen(
                            navController = mainNavController,
                            placesViewModel = placesViewModel,
                        )

                    }

                    composable(
                        MainNavigation.CheckInUser.route +
                                USERS_CHECK_IN_ROUTE,
                        arguments = listOf(
                            navArgument(NAV_ARG_PLACE_TYPE) {
                                type = NavType.StringType
                            },
                            navArgument(NAV_ARG_PLACE_NAME) {
                                type = NavType.StringType
                            },
                            navArgument(NAV_ARG_CITY_NAME) {
                                type = NavType.StringType
                            },
                        )
                    ) { navBackStackEntry ->
                        val placeType = navBackStackEntry.arguments?.getString(NAV_ARG_PLACE_TYPE) ?: ""
                        val placeName = navBackStackEntry.arguments?.getString(NAV_ARG_PLACE_NAME) ?: ""
                        val addressCity =
                            navBackStackEntry.arguments?.getString(NAV_ARG_CITY_NAME) ?: ""
                        val usersCheckinViewModel: UsersCheckInViewModel = hiltViewModel()


                        UsersCheckInScreen(
                            placeName = placeName,
                            placeType = placeType,
                            addressCity = addressCity,
                            navController = mainNavController,
                            usersCheckInViewModel = usersCheckinViewModel,
                            isDialogOpen = isDialogOpen,
                        )
                    }

                    composable(
                        MainNavigation.UserCheckInProfile.route +
                        USER_CHECK_IN_PROFILE_ROUTE,
                        arguments = listOf(
                            navArgument(USER_CHECK_IN_ID) {
                                type = NavType.LongType
                            },
                        )
                        ){ navBackStackEntry ->

                        val userCheckinId = navBackStackEntry
                            .arguments?.getLong(USER_CHECK_IN_ID) ?: 0
                        val userCheckinProfileViewModel: UserCheckInProfileViewModel = hiltViewModel()
                        CheckInProfileScreen(
                            userId = userCheckinId,
                            navController = mainNavController,
                            userCheckInProfileViewModel = userCheckinProfileViewModel
                        )
                    }
                }

                composable(MainNavigation.Profile.route) {
                    val userProfileViewModel: UserProfileViewModel = hiltViewModel()
                    UserProfileScreen(
                        userProfileViewModel = userProfileViewModel
                    )
                }

                composable(MainNavigation.Messaging.route) {
                    val messagingViewModel: MessagingViewModel = hiltViewModel()
                    MessagingScreen(
                        navController = mainNavController,
                        messagingViewModel = messagingViewModel
                    )
                }


            }
        }

    }
}