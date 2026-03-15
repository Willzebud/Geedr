package com.app_will.geedrapplication.network.dto

import com.squareup.moshi.Json

data class UserDto(
    @Json(name = "id")
    val userId: String,

    @Json(name = "user_name")
    val userName: String,

    @Json(name = "user_lastname")
    val userLastname: String,

    @Json(name = "user_birth_date")
    val userAge: Int,

    @Json(name = "user_email")
    val userEmail: String,

    @Json(name = "user_phone_number")
    val userPhoneNumber: String,

    @Json(name = "user_password")
    val userPassword: String,

    @Json(name = "user_gender")
    val userGender: String,

    @Json(name = "user_sexual_orientation")
    val userSexualOrientation: String,

    @Json(name = "user_picture_url")
    val userPicture: String,

    @Json(name = "user_checkin")
    val isUserCheckin: Boolean,

    @Json(name = "user_job")
    val userJob: String,

    @Json(name = "user_about_me")
    val userAboutMe: List<String>,

    @Json(name = "user_passions")
    val userPassions: List<String>,

    @Json(name = "user_role")
    val userRole: String,

    @Json(name = "place_number")
    val placeNumber: Int,

    @Json(name = "is_visible")
    val isUserVisible: Boolean,

    @Json(name = "user_status")
    val userStatus: String,

    @Json(name = "user_latitude")
    val userLatitude: Double,

    @Json(name = "user_longitude")
    val userLongitude: Double,

    @Json(name = "user_radius_m")
    val userRadiusM: Int
)
