package com.example.jonathansnidervirginmoney.data.repository

import com.example.jonathansnidervirginmoney.data.api.APIClient
import com.example.jonathansnidervirginmoney.data.api.APIDetails
import com.example.jonathansnidervirginmoney.data.model.Rooms
import com.example.jonathansnidervirginmoney.data.model.Users
import com.example.jonathansnidervirginmoney.data.model.UsersItemModel
import retrofit2.http.GET
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    val apiClient: APIClient
): Repository {
    @GET(APIDetails.ENDPOINT_ROOMS)
    override suspend fun getRooms(): Rooms {
        return apiClient.getRooms()
    }
    @GET(APIDetails.ENDPOINT_USERS)
    override suspend fun getUsers(): Users {
        return apiClient.getUsers()
    }

    @GET("people/{id}")
    override suspend fun getUserById(id: String): UsersItemModel {
        return apiClient.getUser(id)
    }
}