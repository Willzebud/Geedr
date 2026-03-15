package com.app_will.geedrapplication.ui.messaging

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.network.dto.MessagesDto
import com.app_will.geedrapplication.ui.theme.GeedrApplicationTheme
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun MessagingScreen(
    navController: NavController,
    messagingViewModel: MessagingViewModel
) {
    val context = LocalContext.current
    val messagesList by messagingViewModel.messageListStateFlow.collectAsState()
    val messageUserImg by messagingViewModel.messageUrlImgStateFlow.collectAsState()
    val messageUserName by messagingViewModel.messageUserNameStateFlow.collectAsState()
    val messageText by messagingViewModel.messageTextStateFlow.collectAsState()


    BackHandler(enabled = true) {
    }

    MessagingContent(
        context = context,
        messageUserImg = messageUserImg,
        messageUserName = messageUserName,
        messageText = messageText,
        messagesList = messagesList
    )
}

@Composable
fun MessagingContent(
    context: Context,
    messagesList: List<MessagesDto>,
    messageUserImg: String,
    messageUserName: String,
    messageText: String,

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd),
                onClick = { }
            ) {
                Text(
                    text = context.getString(R.string.close),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            modifier = Modifier
        ) {
            Column {
                Row {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Likes reçus en attente de votre réponse",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Row {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .width(80.dp)
                    ) {
                        AsyncImage(
                            model = "https://image2url.com/r2/default/images/1773231995911-adf6f208-ad74-47cc-9477-2828e4c43e07.jpg",
                            error = painterResource(R.drawable.baseline_error_24),
                            placeholder = painterResource(R.drawable.baseline_error_24),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.padding(20.dp))
            Column {
                Row {
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Vos Match et message",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
                LazyColumn {
                    items(items = messagesList) { message ->
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview(
) {
    GeedrApplicationTheme {
        val context = LocalContext.current
        //MessagingContent(context = context)
    }
}