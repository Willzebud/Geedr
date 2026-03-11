package com.app_will.geedrapplication.ui.main

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.CenterAlignedTopAppBar
import com.app_will.geedrapplication.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app_will.geedrapplication.ui.components.BottomNavigationBar
import com.app_will.geedrapplication.network.dto.PlacesDto
import com.app_will.geedrapplication.ui.components.ButtonGoogleMap
import com.app_will.geedrapplication.ui.components.ProgressBar
import com.app_will.geedrapplication.ui.components.TitleScreen
import com.app_will.geedrapplication.utils.UiEvent
import com.app_will.geedrapplication.utils.actualGeoLocalisation
import com.app_will.geedrapplication.utils.openGoogleMap
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val places by mainViewModel.placesListStateFlow.collectAsState()
    val isProgressBarActiveStateFlow by mainViewModel.isProgressBarActiveStateFlow.collectAsState()
    val isRefreshing by mainViewModel.isRefreshingStateFlow.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        mainViewModel.responseUserStateFlow.collect { event ->
            when (event) {
                is UiEvent.SnackBar -> {
                    scope.launch {
                        val message = context.getString(event.message)
                        snackBarHostState.showSnackbar(message = message)
                    }
                }

                else -> snackBarHostState.showSnackbar(
                    message = context.getString(R.string.error_occurred
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.navigateToCheckinProfileSharedFlow.collect {
            navController.navigate(it)
        }
    }


    MainContent(
        navController = navController,
        placesList = places,
        context = context,
        snackBarHostState = snackBarHostState,
        isProgressBarActive = isProgressBarActiveStateFlow,
        swipeRefreshState = swipeRefreshState,
        onRefresh = { mainViewModel.swipeRefresh() },
        onNavigateChekinProfile = { placeType, placeName, addressCity, userCheckin ->
            mainViewModel.navigateToUsersCheckinProfile(
                placeType,
                placeName,
                addressCity,
                userCheckin
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navController: NavController,
    placesList: List<PlacesDto>,
    context: Context,
    snackBarHostState: SnackbarHostState,
    isProgressBarActive: Boolean,
    swipeRefreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    onNavigateChekinProfile: (String, String, String, Int) -> Unit
) {


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(

                title = {
                    TitleScreen(
                        title = stringResource(id = R.string.app_name),
                        text = stringResource(id = R.string.main_second_title),
                        modifier = Modifier
                    )
                },
                modifier = Modifier
                    .padding(20.dp)
            )
        },

        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padding ->

        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
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
                                Text(
                                    text = places.userCheckin.toString(),
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
                                        onNavigateChekinProfile(
                                            places.placeType,
                                            places.placeName,
                                            places.addressCity,
                                            places.userCheckin
                                        )
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = R.drawable.arrow_forward_ios_24dp_f88e8e
                                        ),
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null,
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(10.dp)
                                ) {
                                    ButtonGoogleMap(
                                        modifier = Modifier
                                    ) { openGoogleMap(context) }
                                    Text(
                                        text = actualGeoLocalisation(
                                            places.placeLatitude,
                                            places.placeLongitude
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
}
