package com.app_will.geedrapplication.repository

import com.app_will.geedrapplication.network.ApiInterface
import com.app_will.geedrapplication.network.dto.MessagesDto
import com.app_will.geedrapplication.network.dto.PlacesDto
import com.app_will.geedrapplication.network.dto.UpdateUserDto
import com.app_will.geedrapplication.network.dto.UserDto
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    suspend fun login(
        email: Long,
        password: String
    ): Response<List<UserDto>> = apiInterface.login(
        email = email,
        password = password
    )

    suspend fun getPlaces(): Response<List<PlacesDto>> = apiInterface.getAllPlaces()

    suspend fun getUsers(): Response<List<UserDto>> = apiInterface.getUsers()

    suspend fun getUser(
        userCheckinId: Long
    ): Response<List<UserDto>> = apiInterface.getUser(userId = userCheckinId)

    suspend fun updateUserVisibility(
        userCheckinId: Long,
        userCheckinVisibility: UpdateUserDto
    ): Response<UserDto> =
        apiInterface.updateUserVisibility(
            id = userCheckinId,
            body = userCheckinVisibility
        )

    suspend fun getMessages(): Response<List<MessagesDto>> = apiInterface.getMessages()

}