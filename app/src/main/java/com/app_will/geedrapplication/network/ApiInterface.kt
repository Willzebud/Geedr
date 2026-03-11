package com.app_will.geedrapplication.network

import com.app_will.geedrapplication.network.dto.PlacesDto
import com.app_will.geedrapplication.network.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiRoutes.POST_LOGIN)
    suspend fun login(
        @Query("user_email") email: String,
        @Query("user_password") password: String
    ): Response<List<UserDto>>

    @GET(ApiRoutes.GET_PLACES)
    suspend fun getAllPlaces(
    ): Response<List<PlacesDto>>

    @GET(ApiRoutes.GET_USERS)
    suspend fun getUsers(
    ): Response<List<UserDto>>
}