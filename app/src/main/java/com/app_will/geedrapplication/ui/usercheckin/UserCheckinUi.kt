package com.app_will.geedrapplication.ui.usercheckin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.network.dto.UserDto
import com.app_will.geedrapplication.ui.components.BottomNavigationBar
import com.app_will.geedrapplication.ui.components.ProgressBar
import com.app_will.geedrapplication.ui.theme.GeedrApplicationTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun UserCheckinScreen(
    placeName: String,
    placeType: String,
    addressCity: String,
    nbruserCheckin: Int,
    navController: NavController,
    userCheckinViewModel: UserCheckinViewModel
) {
    val context = LocalContext.current
    val isProgressBarActive by userCheckinViewModel.isProgressBarActiveStateFlow.collectAsState()
    val isRefreshing by userCheckinViewModel.isRefreshingStateFlow.collectAsState()
    val users by userCheckinViewModel.usersListStateFlow.collectAsState()
    val usersCheckin by userCheckinViewModel.userCheckinFilterStateFlow.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    UserCheckinContent(
        placeName = placeName,
        placeType = placeType,
        addressCity = addressCity,
        nbruserCheckin = nbruserCheckin,
        userProfile = usersCheckin,
        isProgressBarActive = isProgressBarActive,
        swipeRefreshState = swipeRefreshState,
        navController = navController,
        onRefresh = { userCheckinViewModel.swipeRefresh() }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCheckinContent(
    placeType: String,
    placeName: String,
    addressCity : String,
    nbruserCheckin: Int,
    userProfile: List<UserDto>,
    isProgressBarActive: Boolean,
    swipeRefreshState: SwipeRefreshState,
    navController: NavController,
    onRefresh: () -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(

                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterStart)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "$placeType : ",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier

                                )
                                Text(
                                    text = placeName,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Ville : ",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier

                                )
                                Text(
                                    text = addressCity,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                        Text(
                            text = "Sortir",
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
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
            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "$nbruserCheckin profils sont actuellements présent",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Envoyez un like pour faire le premier pas",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { onRefresh() }
                ) {


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .padding(4.dp),
                    ) {
                        items(items = userProfile) { userProfile ->


                            Row(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .border(
                                        width = 0.dp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {

                                Box(
                                    modifier = Modifier
                                        .height(170.dp)
                                        .width(150.dp)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(userProfile.userPicture)
                                            .crossfade(true)
                                            .build(),
                                        error = painterResource(R.drawable.baseline_error_24),
                                        placeholder = painterResource(R.drawable.baseline_error_24),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(11.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = "${userProfile.userAge}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp)
                                    )
                                }


                            }
                        }
                    }
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    GeedrApplicationTheme {

        val fakeUsers = listOf(
            UserDto(
                id = "1",
                userName = "Alice",
                userPicture = "",
                userAge = 28,
                isUserCheckin = true,
                userEmail = "blabla",
                userPassword = "tesst",
                userGender = "FEMELE",
                userJob = "dev",
                userLastname = "t",
                userLatitude = 2330.0,
                userLongitude = 3234.0,
                userPassions = listOf("", ""),
                userPhoneNumber = "0000",
                userPoliticChoice = "gauche",
                userRadiusM = 2,
                userRole = "rt",
                userStatus = "dfg"
            ),
            UserDto(
                id = "2",
                userName = "Emma",
                userPicture = "",
                userAge = 31,
                isUserCheckin = true,
                userEmail = "blabla",
                userPassword = "tesst",
                userGender = "FEMELE",
                userJob = "dev",
                userLastname = "t",
                userLatitude = 2330.0,
                userLongitude = 3234.0,
                userPassions = listOf("", ""),
                userPhoneNumber = "0000",
                userPoliticChoice = "gauche",
                userRadiusM = 2,
                userRole = "rt",
                userStatus = "dfg"
            )
        )

    }
}