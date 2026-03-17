package com.app_will.geedrapplication.ui.places

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.data.dto.PlacesDto
import com.app_will.geedrapplication.ui.components.ButtonMap
import com.app_will.geedrapplication.ui.components.ProgressBar
import com.app_will.geedrapplication.ui.components.TitleScreen
import com.app_will.geedrapplication.utils.IS_USER_CHECK_IN_UPDATED
import com.app_will.geedrapplication.utils.USER_CHECK_IN_NUMBER
import com.app_will.geedrapplication.utils.UiEvent
import com.app_will.geedrapplication.utils.actualGeoLocalisation
import com.app_will.geedrapplication.utils.openGoogleMap
import com.app_will.geedrapplication.utils.showToast
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun PlacesScreen(
    navController: NavController,
    placesViewModel: PlacesViewModel,

    ) {
    val context = LocalContext.current
    val places by placesViewModel.placesListStateFlow.collectAsState()
    val isProgressBarActiveStateFlow by placesViewModel.isProgressBarActiveStateFlow.collectAsState()
    val isRefreshing by placesViewModel.isRefreshingStateFlow.collectAsState()
    val userCheckInNumber by placesViewModel.userCheckInActive.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    val userCheckInNumberUpdated = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(IS_USER_CHECK_IN_UPDATED, false)
        ?.collectAsState()

    LaunchedEffect(userCheckInNumberUpdated){
        placesViewModel.getPlaces()
    }

    LaunchedEffect(Unit) {
        placesViewModel.responseUserStateFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    context.showToast(context, event.message)
                }

                else -> context.showToast(context, R.string.error_occurred)
            }
        }
    }

    LaunchedEffect(Unit) {
        placesViewModel.navigateToCheckInProfilesSharedFlow.collect {
            navController.navigate(it)
        }
    }

    PlacesContent(
        placesList = places,
        context = context,
        isProgressBarActive = isProgressBarActiveStateFlow,
        swipeRefreshState = swipeRefreshState,
        userCheckInNumber = userCheckInNumber,
        onRefresh = { placesViewModel.swipeRefresh() },
        onNavigateCheckInProfile = { placeType, placeName, addressCity ->
            placesViewModel.navigateToUsersCheckIn(
                placeType,
                placeName,
                addressCity
            )
        }
    )
}


@Composable
fun PlacesContent(
    placesList: List<PlacesDto>,
    context: Context,
    isProgressBarActive: Boolean,
    swipeRefreshState: SwipeRefreshState,
    userCheckInNumber: Int,
    onRefresh: () -> Unit,
    onNavigateCheckInProfile: (String, String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleScreen(
            title = stringResource(id = R.string.app_name),
            text = stringResource(id = R.string.main_second_title),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { onRefresh() }
        ) {
            LazyColumn {
                items(items = placesList) { places ->
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "${places.placeType} " +
                                            context.getString(R.string.main_type_of_places),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold

                                )
                                Text(
                                    text = " ${places.placeName}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "${context.getString(R.string.main_city)} ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold

                                )
                                Text(
                                    text = places.addressCity,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .width(60.dp)
                                .padding(6.dp)
                        ) {
                            if (places.id == "2")
                                Text(
                                    text = "${userCheckInNumber + places.userCheckin}",
                                    color = MaterialTheme.colorScheme.primary
                                )
                             else
                                Text(
                                    text = "$userCheckInNumber",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.people_24dp_f88e8e
                                ),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        if (places.isUserThere) {
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .width(35.dp)
                                    .height(35.dp),
                                onClick = {
                                    onNavigateCheckInProfile(
                                        places.placeType,
                                        places.placeName,
                                        places.addressCity
                                    )
                                },
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.arrow_forward_ios_24dp_f88e8e
                                    ),
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(40.dp)
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(10.dp)
                            ) {
                                ButtonMap(
                                    context = context,
                                    modifier = Modifier
                                ) { openGoogleMap(context) }
                                Text(
                                    text = actualGeoLocalisation(
                                        context,
                                        places.placeLatitude,
                                        places.placeLongitude,
                                    ),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)

                                )
                            }
                        }
                        ProgressBar(
                            isProgressBarActive = isProgressBarActive,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }


                }
            }

        }

    }


}