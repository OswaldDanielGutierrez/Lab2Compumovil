package com.example.jetsnack.api


import Snack
import com.example.jetsnack.model.user
import retrofit2.http.GET

interface apiService {
    @GET("get-snacks")
    suspend fun getSnacks(): List<Snack>

    @GET("get-users")
    suspend fun getUsers(): List<user>
}