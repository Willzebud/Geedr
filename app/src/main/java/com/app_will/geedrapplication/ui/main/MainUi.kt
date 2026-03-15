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
import androidx.compose.material3.Text
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
import com.app_will.geedrapplication.ui.userscheckin.UsersCheckinScreen
import com.app_will.geedrapplication.ui.userscheckin.UsersCheckinViewModel
import com.app_will.geedrapplication.ui.usercheckinprofile.CheckinProfileScreen
import com.app_will.geedrapplication.ui.usercheckinprofile.UserCheckinProfileViewModel
import com.app_will.geedrapplication.ui.userprofil.UserProfileScreen
import com.app_will.geedrapplication.ui.userprofil.UserProfileViewModel
import com.app_will.geedrapplication.utils.NAV_ARG_CITY_NAME
import com.app_will.geedrapplication.utils.NAV_ARG_PLACE_NAME
import com.app_will.geedrapplication.utils.NAV_ARG_PLACE_TYPE
import com.app_will.geedrapplication.utils.USER_CHECKIN_ID
import com.app_will.geedrapplication.utils.USER_CHECKIN_PROFILE_ROUTE
import com.app_will.geedrapplication.utils.USERS_CHECKIN_ROUTE

@Composable
fun MainScreen(
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showDialog = remember { mutableStateOf(false) }
    val notification = remember { mutableStateOf(false) }

    val isBottomBarVisible = when (currentDestination?.route ) {
        MainNavigation.UserCheckinProfil.route + USER_CHECKIN_PROFILE_ROUTE -> false
        else -> true
    }

    MainContent(
        isBottomBarVisible = isBottomBarVisible,
        currentDestination = currentDestination,
        mainNavController = mainNavController,
        isDialogOpen = showDialog,
        isLiked = notification,
        onOpenDialog = {
            showDialog.value = true
            notification.value = true
        },

    )
}

@Composable
fun MainContent(
    isBottomBarVisible: Boolean,
    currentDestination: NavDestination?,
    mainNavController : NavHostController,
    isLiked: MutableState<Boolean>,
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
                        isLiked = isLiked
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
                        MainNavigation.CheckinUser.route +
                                USERS_CHECKIN_ROUTE,
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
                        val usersCheckinViewModel: UsersCheckinViewModel = hiltViewModel()


                        UsersCheckinScreen(
                            placeName = placeName,
                            placeType = placeType,
                            addressCity = addressCity,
                            navController = mainNavController,
                            usersCheckinViewModel = usersCheckinViewModel,
                            isDialogOpen = isDialogOpen,
                        )
                    }

                    composable(
                        MainNavigation.UserCheckinProfil.route +
                        USER_CHECKIN_PROFILE_ROUTE,
                        arguments = listOf(
                            navArgument(USER_CHECKIN_ID) {
                                type = NavType.LongType
                            },
                        )
                        ){ navBackStackEntry ->

                        val userCheckinId = navBackStackEntry
                            .arguments?.getLong(USER_CHECKIN_ID) ?: 0
                        val userCheckinProfileViewModel: UserCheckinProfileViewModel = hiltViewModel()
                        CheckinProfileScreen(
                            userId = userCheckinId,
                            navController = mainNavController,
                            userCheckinProfileViewModel = userCheckinProfileViewModel
                        )
                    }
                }

                composable(MainNavigation.Profile.route) {
                    val userProfileViewModel: UserProfileViewModel = hiltViewModel()
                    UserProfileScreen(
                        navController = mainNavController,
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