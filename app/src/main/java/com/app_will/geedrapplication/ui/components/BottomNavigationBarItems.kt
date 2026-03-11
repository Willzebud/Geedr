package com.app_will.geedrapplication.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.ScreenNavigation

data class BottomNavigationBarItems(
    val icon: Painter,
    val route: String
)

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    val navigationItems = listOf(
        BottomNavigationBarItems(
            icon = painterResource(
                id = R.drawable.favorite_24dp_f88e8e
            ),
            route = ScreenNavigation.Main.route
        ),
        BottomNavigationBarItems(
            icon = painterResource(
                id = R.drawable.chat_bubble_24dp_f88e8e
            ),
            route = ScreenNavigation.Messaging.route
        ),
        BottomNavigationBarItems(
            icon = painterResource(
                id = R.drawable.account_box_35dp_f88e8e
            ),
            route = ScreenNavigation.Profile.route
        )
    )

    NavigationBar(
    ) {
        navigationItems.forEachIndexed { index, items ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(items.route){
                        this.restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = items.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .width(35.dp)
                            .height(35.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}


