package com.example.jetsnack.ui

import Snack
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jetsnack.SnackApplication
import com.example.jetsnack.data.SnackRepository

import com.example.jetsnack.model.user
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SnackUiState {
    data class Success(
        val snacks: List<Snack>,
        val users: List<user>,
        val loggedInUser: user? = null
    ): SnackUiState
    object Error : SnackUiState
    object Loading : SnackUiState
}

class SnackViewModel(private val snackRepository: SnackRepository): ViewModel() {
    var snackUiState: SnackUiState by mutableStateOf(SnackUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData(){
        Log.i("MyTag", "Getting data...")
        viewModelScope.launch {
            snackUiState = SnackUiState.Loading
            try {
                Log.i("MyTag", "Try getting data...")
                snackUiState = SnackUiState.Success(snackRepository.getSnacks() , snackRepository.getUsers())
                Log.i("MyTag", "Success getting data...")
                Log.i("MyTag", snackUiState.toString())
            } catch (e: IOException) {
                Log.i("MyTag", "Error IOException...")
                Log.i("MyTag", e.message.toString())
                snackUiState = SnackUiState.Error
            } catch (e: HttpException) {
                Log.i("MyTag", "Error HttpException...")
                Log.i("MyTag", e.message.toString())
                snackUiState = SnackUiState.Error
            }
            Log.i("MyTag", "End getting data...")
        }
    }

    fun getSnacks(): List<Snack> {
        return when (snackUiState) {
            is SnackUiState.Success -> (snackUiState as SnackUiState.Success).snacks
            else -> emptyList()
        }
    }

    fun getUsers(): List<user> {
        return when (snackUiState) {
            is SnackUiState.Success -> (snackUiState as SnackUiState.Success).users
            else -> emptyList()
        }
    }

    fun logIn(email: String, password: String): Boolean {
        for (user in getUsers()) {
            if (user.email == email && user.password == password) {
                snackUiState = SnackUiState.Success(getSnacks(), getUsers(), user)
                return true
            }
        }
        return false
    }

    fun logOut() {
        snackUiState = SnackUiState.Success(getSnacks(), getUsers())
    }

    fun getLoggedInUser(): user? {
        return when (snackUiState) {
            is SnackUiState.Success -> (snackUiState as SnackUiState.Success).loggedInUser
            else -> null
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SnackApplication)
                val snackRepository = application.container.snackRepository
                SnackViewModel(snackRepository = snackRepository)
            }
        }
    }
}