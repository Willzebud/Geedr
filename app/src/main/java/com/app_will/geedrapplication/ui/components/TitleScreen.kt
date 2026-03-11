package com.app_will.geedrapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_will.geedrapplication.ui.theme.GeedrApplicationTheme

@Composable
fun TitleScreen(
    title: String,
    text: String,
    modifier: Modifier
){

    Column {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )

    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeedrApplicationTheme {
        TitleScreen(title = "Ok", text = "Test", modifier = Modifier)
    }
}