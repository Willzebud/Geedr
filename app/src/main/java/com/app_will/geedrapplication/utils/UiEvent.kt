package com.app_will.geedrapplication.utils

sealed class UiEvent {
    data class ShowToast(val message: Int) : UiEvent()
    data class SnackBar(val message: Int) : UiEvent()
}