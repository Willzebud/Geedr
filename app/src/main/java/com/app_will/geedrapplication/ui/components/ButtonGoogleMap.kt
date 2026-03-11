package com.app_will.geedrapplication.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_will.geedrapplication.ui.main.MainContent
import com.app_will.geedrapplication.ui.theme.GeedrApplicationTheme

@Composable
fun ButtonGoogleMap(
    modifier: Modifier = Modifier,
    onOpenGoogleMap: () -> Unit,
){
    androidx.compose.material3.Button(
        modifier = modifier
            .height(35.dp)
            .width(115.dp),
        onClick =  onOpenGoogleMap,
        enabled = true
    ) {
        Text(
            text = "Google Map",
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    GeedrApplicationTheme {

    }
}