package com.app_will.geedrapplication.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.app_will.geedrapplication.R

@Composable
fun Dialog(
    context: Context,
    modifier: Modifier,
    dialogType: String,
    userImg: String,
    dialogText: String,
    dialogIconText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
){
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest ,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        ),
    ) {
        Card(
            colors = CardColors(
                containerColor = Color.White,
                contentColor = Color.White,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .width(400.dp)
                .height(600.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Color.White)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()

            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = dialogType,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = modifier
                            .padding(20.dp)
                    )

                    AsyncImage(
                        model = userImg,
                        contentDescription = null,
                        modifier = modifier
                            .height(400.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .height(150.dp)
                ) {
                    Text(
                        dialogText,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .padding(20.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text(
                                context.getString(R.string.close),
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        TextButton(onClick = onConfirmation) {
                            Text(
                                dialogIconText,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

            }
        }
    }
}