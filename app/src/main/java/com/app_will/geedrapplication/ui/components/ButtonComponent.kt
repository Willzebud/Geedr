package com.app_will.geedrapplication.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    onClickNavigate: () -> Unit,
    isEnabled: Boolean = true,
    label: String
){
    androidx.compose.material3.Button(
        modifier = modifier,
        onClick =  onClickNavigate,
        enabled = isEnabled
    ) {
        Text(text = label)
    }
}