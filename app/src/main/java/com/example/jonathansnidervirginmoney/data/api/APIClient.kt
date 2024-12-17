package com.example.jonathansnidervirginmoney.data.api

import com.example.jonathansnidervirginmoney.data.model.Rooms
import com.example.jonathansnidervirginmoney.data.model.Users
import com.example.jonathansnidervirginmoney.data.model.UsersItemModel
import retrofit2.http.GET
import retrofit2.http.Path

interface APIClient {
    @GET(APIDetails.ENDPOINT_ROOMS)
    suspend fun getRooms(
//if parameters were to be passed to the api call (like an ID) they would be placed here
    ): Rooms

    @GET(APIDetails.ENDPOINT_USERS)
    suspend fun getUsers(

    ):Users

    @GET("people/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): UsersItemModel
}