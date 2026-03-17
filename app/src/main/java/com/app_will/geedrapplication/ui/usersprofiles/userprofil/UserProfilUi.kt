package com.app_will.geedrapplication.ui.usersprofiles.userprofil

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.utils.UiEvent
import com.app_will.geedrapplication.utils.showToast

@Composable
fun UserProfileScreen(
    userProfileViewModel: UserProfileViewModel
){
    val context = LocalContext.current
    val userName by userProfileViewModel.userName.collectAsState()
    val userAge by userProfileViewModel.userAge.collectAsState()
    val userGender by userProfileViewModel.userGender.collectAsState()
    val userSexualOrientation by userProfileViewModel.userSexualOrientation.collectAsState()
    val userJob by userProfileViewModel.userJob.collectAsState()
    val userAboutMe by userProfileViewModel.userAboutMe.collectAsState()
    val userImgProfile by userProfileViewModel.userImgProfile.collectAsState()
    val userPassions by userProfileViewModel.userPassions.collectAsState()

    BackHandler(enabled = true) {
    }

    LaunchedEffect(Unit) {
        userProfileViewModel.responseUserSharedFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    context.showToast(context, event.message)
                }
                else -> context.showToast(context, R.string.error_occurred)
            }
        }
    }

    LaunchedEffect(Unit) {
        userProfileViewModel.getUserProfile()
    }

    UserProfileContent(
        context = context,
        userName = userName,
        userAge = userAge,
        userGender = userGender,
        userSexualOrientation = userSexualOrientation,
        userJob = userJob,
        userAboutMe = userAboutMe,
        userImgProfile = userImgProfile,
        userPassions = userPassions
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileContent(
    context: Context,
    userName: String,
    userAge: String,
    userGender: String,
    userSexualOrientation: String,
    userJob: String,
    userAboutMe: List<String>,
    userImgProfile: String,
    userPassions: List<String>
){
    Scaffold(
    ) { paddingValues ->
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
                    error = painterResource(R.drawable.baseline_error_24),
                    placeholder = painterResource(R.drawable.baseline_error_24),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
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
                        text = userName,
                        fontSize = 24.sp,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "$userAge ${context.getString(R.string.age)}",
                        fontSize = 24.sp,
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
    }
}