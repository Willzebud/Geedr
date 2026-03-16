package com.app_will.geedrapplication.data.network

import com.app_will.geedrapplication.data.dto.MessagesDto
import com.app_will.geedrapplication.data.dto.PlacesDto
import com.app_will.geedrapplication.data.dto.UpdateLikeStatus
import com.app_will.geedrapplication.data.dto.UpdateUserDto
import com.app_will.geedrapplication.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiRoutes.POST_LOGIN)
    suspend fun login(
        @Query("user_email") email: Long,
        @Query("user_password") password: String
    ): Response<List<UserDto>>

    @GET(ApiRoutes.GET_PLACES)
    suspend fun getAllPlaces(
    ): Response<List<PlacesDto>>

    @GET(ApiRoutes.GET_USERS)
    suspend fun getUsers(
    ): Response<List<UserDto>>

    @GET(ApiRoutes.GET_USER)
    suspend fun getUser(
        @Query("id") userId: Long,
    ): Response<List<UserDto>>

    @PATCH(ApiRoutes.PATCH_UPDATE_USER)
    suspend fun updateUserVisibility(
        @Path("id") id: Long,
        @Body body: UpdateUserDto
    ): Response<UserDto>

    @GET(ApiRoutes.GET_MESSAGES)
    suspend fun getMessages(
    ): Response<List<MessagesDto>>

    @PATCH(ApiRoutes.PATCH_UPDATE_LIKE)
    suspend fun updateLikeVisibility(
        @Path("id") id: Long,
        @Body body: UpdateLikeStatus
    ): Response<MessagesDto>
}