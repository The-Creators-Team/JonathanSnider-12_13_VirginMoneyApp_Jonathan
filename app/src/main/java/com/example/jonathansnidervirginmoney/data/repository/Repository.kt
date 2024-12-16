package com.example.jonathansnidervirginmoney.data.repository

import com.example.jonathansnidervirginmoney.data.model.Rooms
import com.example.jonathansnidervirginmoney.data.model.Users

interface Repository {

    suspend fun getRooms():Rooms
    suspend fun getUsers():Users
}