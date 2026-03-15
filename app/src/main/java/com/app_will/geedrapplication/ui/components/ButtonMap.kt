package com.app_will.geedrapplication.ui.components

import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_will.geedrapplication.R
import com.app_will.geedrapplication.ui.theme.GeedrApplicationTheme

@Composable
fun ButtonMap(
    context: Context,
    modifier: Modifier = Modifier,
    onOpenGoogleMap: () -> Unit,
){
    androidx.compose.material3.Button(
        modifier = modifier
            .height(35.dp)
            .width(80.dp),
        onClick =  onOpenGoogleMap,
        enabled = true
    ) {
        Text(
            text = context.getString(R.string.map),
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