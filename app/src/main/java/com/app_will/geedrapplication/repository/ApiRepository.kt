package com.app_will.geedrapplication.repository

import com.app_will.geedrapplication.network.ApiInterface
import com.app_will.geedrapplication.network.dto.PlacesDto
import com.app_will.geedrapplication.network.dto.UserDto
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    suspend fun login(
        email: String,
        password: String
    ): Response<List<UserDto>> = apiInterface.login(
        email = email,
        password = password
    )

    suspend fun getPlaces(): Response<List<PlacesDto>> = apiInterface.getAllPlaces()

    suspend fun getUsers(): Response<List<UserDto>> = apiInterface.getUsers()
}