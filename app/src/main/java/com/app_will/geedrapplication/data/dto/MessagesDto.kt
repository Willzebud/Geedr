package com.app_will.geedrapplication.data.dto

import com.squareup.moshi.Json

data class MessagesDto(
    @Json(name = "id")
    val id: Long,

    @Json(name = "message_picture")
    val messagePicture: String,

    @Json(name = "message_name")
    val messageName: String,

    @Json(name = "message_text")
    val messageText: String,

    @Json(name = "is_visible")
    val isVisible: Boolean
)
