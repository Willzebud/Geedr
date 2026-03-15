package com.app_will.geedrapplication.ui.userscheckin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.network.dto.UserDto
import com.app_will.geedrapplication.ui.components.Dialog
import com.app_will.geedrapplication.ui.components.ProgressBar
import com.app_will.geedrapplication.utils.USER_CHECKIN_ID_LIKE
import com.app_will.geedrapplication.utils.USER_CHECKIN_NUMBER
import com.app_will.geedrapplication.utils.USER_CHECKIN_UPDATED
import com.app_will.geedrapplication.utils.UiEvent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun UsersCheckinScreen(
    placeName: String,
    placeType: String,
    addressCity: String,
    navController: NavController,
    usersCheckinViewModel: UsersCheckinViewModel,
    isDialogOpen: MutableState<Boolean>,
) {
    val context = LocalContext.current
    val isProgressBarActive by usersCheckinViewModel.isProgressBarActiveStateFlow.collectAsState()
    val isRefreshing by usersCheckinViewModel.isRefreshingStateFlow.collectAsState()
    val usersCheckin by usersCheckinViewModel.userCheckinFilterStateFlow.collectAsState()
    val userCheckinNumber by usersCheckinViewModel.userCheckinActive.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    val userCheckinUpdated = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(USER_CHECKIN_UPDATED, false)
        ?.collectAsState()

    LaunchedEffect(userCheckinUpdated) {
        usersCheckinViewModel.getUsers()
    }

    LaunchedEffect(Unit) {
        usersCheckinViewModel.responseUserStateFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                else -> Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT).show()
            }
        }
    }


    LaunchedEffect(Unit) {
        usersCheckinViewModel.navigateToScreen.collect { checkinProfil ->
            navController.navigate(checkinProfil)
        }
    }

    UsersCheckinContent(
        placeName = placeName,
        placeType = placeType,
        addressCity = addressCity,
        nbrUserCheckin = userCheckinNumber,
        context = context,
        userProfile = usersCheckin,
        isDialogOpen = isDialogOpen,
        isProgressBarActive = isProgressBarActive,
        swipeRefreshState = swipeRefreshState,
        onRefresh = { usersCheckinViewModel.swipeRefresh() },
        onClickBackToPlace = {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                USER_CHECKIN_NUMBER,
                userCheckinNumber
            )
            navController.popBackStack()
        },
        onNavigateToCheckinProfil = { userId ->
            usersCheckinViewModel.navigateToScreen(userId)
        }
    )


}


@Composable
fun UsersCheckinContent(
    placeType: String,
    placeName: String,
    addressCity: String,
    nbrUserCheckin: Int,
    context: Context,
    isDialogOpen: MutableState<Boolean>,
    userProfile: List<UserDto>,
    isProgressBarActive: Boolean,
    swipeRefreshState: SwipeRefreshState,
    onRefresh: () -> Unit,
    onClickBackToPlace: () -> Unit,
    onNavigateToCheckinProfil: (String) -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterStart)
                    .height(60.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "$placeType " + context.getString(R.string.main_type_of_places),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier

                    )
                    Text(
                        text = " $placeName",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = context.getString(R.string.main_city),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier

                    )
                    Text(
                        text = " $addressCity",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = onClickBackToPlace
            ) {
                Text(
                    text = context.getString(R.string.get_out),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "$nbrUserCheckin ${context.getString(R.string.nbr_profiles_in)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = context.getString(R.string.send_like_info),
                    color = MaterialTheme.colorScheme.secondary,
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
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {

                                Box(
                                    modifier = Modifier
                                        .height(170.dp)
                                        .width(150.dp)
                                        .clickable { onNavigateToCheckinProfil(userProfile.userId) }

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
                                        text = "${userProfile.userAge} " +
                                                context.getString(R.string.age),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
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
            when {
                isDialogOpen.value -> {
                    Dialog(
                        context = context,
                        modifier = Modifier,
                        dialogType = context.getString(R.string.dialog_title_like),
                        userImg = context.getString(R.string.dialog_img_url),
                        dialogText = context.getString(R.string.dialog_text_like),
                        onConfirmation = {
                            onNavigateToCheckinProfil(USER_CHECKIN_ID_LIKE)
                            isDialogOpen.value = false
                        },
                        dialogIconText = context.getString(R.string.dialog_text_icon_like),
                        onDismissRequest = { isDialogOpen.value = false }
                    )
                }
            }

        }
    }


}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Surface(
        onClick = {},
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
    ) {

    }
}