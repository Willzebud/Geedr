package com.app_will.geedrapplication.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.utils.NOTIFICATION_NBR
import com.app_will.geedrapplication.utils.USERS_CHECKIN_ROUTE

data class BottomNavigationBar(
    val icon: Painter,
    val route: String
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination,
    isLiked: MutableState<Boolean>
) {

    val secondRoute = when(currentDestination.route){
        MainNavigation.Places.route -> MainNavigation.Places.route
        MainNavigation.CheckinUser.route + USERS_CHECKIN_ROUTE ->
            MainNavigation.CheckinUser.route + USERS_CHECKIN_ROUTE
        else -> MainNavigation.Places.route
    }

    val navigationItems = listOf(
        BottomNavigationBar(
            icon = painterResource(
                id = R.drawable.favorite_24dp_f88e8e
            ),
            route = secondRoute
        ),
        BottomNavigationBar(
            icon = painterResource(
                id = R.drawable.chat_bubble_24dp_f88e8e
            ),
            route = MainNavigation.Messaging.route
        ),
        BottomNavigationBar(
            icon =  painterResource(
                id = R.drawable.circle_80dp_f88e8e
            ),
            route = MainNavigation.Profile.route
        )
    )

    NavigationBar(
    ) {
        navigationItems.forEachIndexed { index, items ->
            NavigationBarItem(
                selected = currentDestination
                    .hierarchy
                    .any {destination -> destination.route == items.route},
                onClick = {
                    if(index == 1) isLiked.value = false
                    navController.navigate(items.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (index == 1 && isLiked.value) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(
                                        text = NOTIFICATION_NBR
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter = items.icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(25.dp)
                                    .height(25.dp)
                            )
                        }
                    } else {
                        Icon(
                            painter = items.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp)
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}


