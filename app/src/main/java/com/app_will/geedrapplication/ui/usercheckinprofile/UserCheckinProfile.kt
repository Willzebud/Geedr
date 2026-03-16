package com.app_will.geedrapplication.ui.usercheckinprofile

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.navigation.MainNavigation
import com.app_will.geedrapplication.ui.components.Dialog
import com.app_will.geedrapplication.utils.USER_CHECK_IN_UPDATED
import com.app_will.geedrapplication.utils.UiEvent
import com.app_will.geedrapplication.utils.showToast

@Composable
fun CheckInProfileScreen(
    userId: Long,
    navController: NavController,
    userCheckInProfileViewModel: UserCheckInProfileViewModel
) {
    BackHandler(enabled = true) {
    }
    val context = LocalContext.current
    val userName by userCheckInProfileViewModel.userCheckInName.collectAsState()
    val userAge by userCheckInProfileViewModel.userCheckInAge.collectAsState()
    val userGender by userCheckInProfileViewModel.userCheckInGender.collectAsState()
    val userSexualOrientation by userCheckInProfileViewModel.userCheckInSexualOrientation.collectAsState()
    val userJob by userCheckInProfileViewModel.userCheckInJob.collectAsState()
    val userAboutMe by userCheckInProfileViewModel.userCheckInAboutMe.collectAsState()
    val userImgProfile by userCheckInProfileViewModel.userCheckInImgProfile.collectAsState()
    val userPassions by userCheckInProfileViewModel.userCheckInPassions.collectAsState()

    val showDialog = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        userCheckInProfileViewModel.getUserCheckInProfile(userId)
    }

    LaunchedEffect(Unit) {
        userCheckInProfileViewModel.responseUserStateFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    context.showToast(context, event.message)
                }

                else -> context.showToast(context, R.string.error_occurred)
            }
        }
    }

    LaunchedEffect(Unit) {
        userCheckInProfileViewModel.navigateToScreenSharedFlow.collect {
            navController.navigate(it.route) {
                popUpTo(MainNavigation.UserCheckInProfile.route) { inclusive = true }
            }
        }
    }


    CheckInProfileContent(
        context = context,
        userId = userId,
        userName = userName,
        userAge = userAge,
        userGender = userGender,
        userSexualOrientation = userSexualOrientation,
        userJob = userJob,
        userAboutMe = userAboutMe,
        userImgProfile = userImgProfile,
        userPassions = userPassions,
        isDialogOpen = showDialog,
        onLikeProfile = { userVisibility ->
            userCheckInProfileViewModel.userCheckInProfileVisibility(
                userCheckInId = userId,
                userCheckInVisibility = userVisibility,
            )
            navController.previousBackStackEntry?.savedStateHandle?.set(
                USER_CHECK_IN_UPDATED,
                true
            )
            navController.popBackStack()
        },
        onDislikeProfile = { userVisibility ->
            userCheckInProfileViewModel.userCheckInProfileVisibility(
                userCheckInId = userId,
                userCheckInVisibility = userVisibility,
            )
            navController.previousBackStackEntry?.savedStateHandle?.set(
                USER_CHECK_IN_UPDATED,
                true
            )
            navController.popBackStack()
        },
        onClickBackToUserCheckin = { navController.popBackStack() },
        onNavigateToMessaging = { userVisibility ->
            userCheckInProfileViewModel.userCheckInProfileVisibility(
                userCheckInId = userId,
                userCheckInVisibility = userVisibility,
            )
            navController.previousBackStackEntry?.savedStateHandle?.set(
                USER_CHECK_IN_UPDATED,
                true
            )
            userCheckInProfileViewModel.navigateToScreen()
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckInProfileContent(
    userId: Long,
    context: Context,
    userName: String,
    userAge: String,
    userGender: String,
    userSexualOrientation: String,
    userJob: String,
    userAboutMe: List<String>,
    userImgProfile: String,
    userPassions: List<String>,
    isDialogOpen: MutableState<Boolean>,
    onLikeProfile: (Boolean) -> Unit,
    onDislikeProfile: (Boolean) -> Unit,
    onClickBackToUserCheckin: () -> Unit,
    onNavigateToMessaging: (Boolean) -> Unit

    ) {
    Scaffold(
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onDislikeProfile(false) },
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close_80dp_f88e8e),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (userId == 6L) {
                            isDialogOpen.value = true
                        } else onDislikeProfile(false)
                    },
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.favorite_24dp_f88e8e),
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = null,
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = userImgProfile,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$userAge ${context.getString(R.string.age)}",
                            fontSize = 24.sp,
                        )
                    }
                    TextButton(
                        onClick = onClickBackToUserCheckin
                    ) {
                        Text(
                            text = context.getString(R.string.close),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = userGender,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary

                    )
                    Text(
                        text = userSexualOrientation,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = userJob,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )

                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = context.getString(R.string.about_me),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(12.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    userAboutMe.forEach { itemsAboutMe ->
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(
                                text = itemsAboutMe,
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(100.dp),
                                    )
                                    .padding(10.dp)
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = context.getString(R.string.my_passions),
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(12.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    userPassions.forEach { passions ->
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Text(
                                text = passions,
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = RoundedCornerShape(100.dp),
                                    )
                                    .padding(10.dp)
                            )
                        }

                    }
                }
            }
            when {
                isDialogOpen.value -> {
                    Dialog(
                        context = context,
                        modifier = Modifier
                            .align(Alignment.Center),
                        dialogType = context.getString(R.string.dialog_title_match),
                        userImg = context.getString(R.string.dialog_img_url),
                        dialogText = context.getString(R.string.dialog_text_match),
                        onConfirmation = { onNavigateToMessaging(false) },
                        dialogIconText = context.getString(R.string.dialog_text_icon_match),
                        onDismissRequest = {
                            isDialogOpen.value = false
                            onLikeProfile(false)
                        }
                    )
                }
            }
        }
    }

}
