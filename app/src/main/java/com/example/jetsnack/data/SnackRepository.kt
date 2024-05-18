package com.example.jetsnack.data

import Snack

import com.example.jetsnack.model.user
import com.example.jetsnack.api.apiService

interface SnackRepository{
    suspend fun getSnacks(): List<Snack>
    suspend fun getUsers(): List<user>
}

class NetworkSnackRepository(
    private val snackApiService: apiService
): SnackRepository {
    override suspend fun getSnacks(): List<Snack> = snackApiService.getSnacks()

    override suspend fun getUsers(): List<user> = snackApiService.getUsers()
}