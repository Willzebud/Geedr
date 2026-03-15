package com.app_will.geedrapplication.network.dto

import com.squareup.moshi.Json

data class UpdateUserDto(
    @Json(name = "is_visible")
    val isUserVisible: Boolean
)
