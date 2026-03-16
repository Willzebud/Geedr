package com.app_will.geedrapplication.network.dto

import com.squareup.moshi.Json

data class UpdateLikeStatus(
    @Json(name = "is_visible")
    val isLikeVisible: Boolean
)
