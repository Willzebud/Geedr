package com.app_will.geedrapplication.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(context: Context, message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}