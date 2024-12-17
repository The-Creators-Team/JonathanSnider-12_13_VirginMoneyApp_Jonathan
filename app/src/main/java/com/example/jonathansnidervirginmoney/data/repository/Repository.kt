package com.example.jonathansnidervirginmoney.data.repository

import com.example.jonathansnidervirginmoney.data.model.Rooms
import com.example.jonathansnidervirginmoney.data.model.Users
import com.example.jonathansnidervirginmoney.data.model.UsersItemModel

interface Repository {

    suspend fun getRooms():Rooms
    suspend fun getUsers():Users
    suspend fun getUserById(id: String):UsersItemModel
}