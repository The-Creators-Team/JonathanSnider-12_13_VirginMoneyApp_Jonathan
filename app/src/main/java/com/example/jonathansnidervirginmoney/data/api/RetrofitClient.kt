package com.example.jonathansnidervirginmoney.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofitInstace= Retrofit.Builder()
        .baseUrl(APIDetails.BASE_URL)
        //.client()
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance= retrofitInstace.create(APIClient::class.java)
}