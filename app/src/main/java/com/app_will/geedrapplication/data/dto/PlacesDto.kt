package com.app_will.geedrapplication.data.dto

import com.squareup.moshi.Json

data class PlacesDto (
    @Json(name = "id")
    val id: String,
    @Json(name = "place_name")
    val placeName: String,
    @Json(name = "place_type")
    val placeType: String,
    @Json(name = "place_latitude")
    val placeLatitude: Double,
    @Json(name = "place_longitude")
    val placeLongitude: Double,
    @Json(name = "is_user_there")
    val isUserThere: Boolean,
    @Json(name = "user_checkin")
    val userCheckin: Int,
    @Json(name = "address_city")
    val addressCity: String,
    @Json(name = "address_zip")
    val addressZip: String,
    @Json(name = "place_radius_m")
    val placeRadiusM: Int
)