package com.app_will.geedrapplication.ui.messaging

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.data.dto.MessagesDto
import com.app_will.geedrapplication.utils.NOTIFICATION_NBR
import com.app_will.geedrapplication.utils.UiEvent
import com.app_will.geedrapplication.utils.showToast

@Composable
fun MessagingScreen(
    navController: NavController,
    messagingViewModel: MessagingViewModel
) {
    val context = LocalContext.current
    val messagesUsersList by messagingViewModel.messageListStateFlow.collectAsState()

    BackHandler(enabled = true) {
    }

    LaunchedEffect(Unit) {
        messagingViewModel.responseUserSharedFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    context.showToast(context, event.message)
                }
                else -> context.showToast(context, R.string.error_occurred)
            }
        }
    }

    LaunchedEffect(Unit) {
        messagingViewModel.navigateToScreen.collect { checkinProfil ->
            navController.navigate(checkinProfil)
        }
    }

    MessagingContent(
        context = context,
        messagesUsersList = messagesUsersList,
        onClickNavigateToProfile = {
            messagingViewModel.likeVisibility(true)
            messagingViewModel.navigateToScreen()

        },
    )
}


@Composable
fun MessagingContent(
    context: Context,
    messagesUsersList: List<MessagesDto>,
    onClickNavigateToProfile: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Column {
                Spacer(modifier = Modifier.padding(10.dp))
                Row {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = context.getString(R.string.messaging_like_received),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Row {
                    Spacer(modifier = Modifier.padding(4.dp))
                    LazyRow {
                        items(items = messagesUsersList) { message ->
                            Box(
                                modifier = Modifier
                                    .height(80.dp)
                                    .width(80.dp)
                            ) {
                                if (!message.isVisible) {
                                    BadgedBox(
                                        badge = {
                                            Badge {
                                                Text(
                                                    text = NOTIFICATION_NBR
                                                )
                                            }
                                        }
                                    ) {
                                        AsyncImage(
                                            model = message.messagePicture,
                                            error = painterResource(R.drawable.baseline_error_24),
                                            placeholder = painterResource(R.drawable.baseline_error_24),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(100.dp))
                                                .clickable { onClickNavigateToProfile() }
                                                .fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Column {
                    Row {
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = context.getString(R.string.messaging_match_message),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    LazyColumn {
                        items(items = messagesUsersList) { message ->
                            if (message.isVisible) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .height(60.dp)
                                            .width(60.dp)
                                    ) {
                                        AsyncImage(
                                            model = message.messagePicture,
                                            error = painterResource(R.drawable.baseline_error_24),
                                            placeholder = painterResource(R.drawable.baseline_error_24),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(100.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = message.messageName,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                            .padding(4.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = message.messageText,
                                        color = MaterialTheme.colorScheme.secondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)

                                    )
                                }
                            }
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 40.dp
                                    )
                                    .size(1.dp)
                                    .background(MaterialTheme.colorScheme.secondary)
                            ) {
                                drawLine(
                                    color = Color.Transparent,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = size.height
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
