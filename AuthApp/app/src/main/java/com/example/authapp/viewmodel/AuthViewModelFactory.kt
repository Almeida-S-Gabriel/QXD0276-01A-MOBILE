package com.example.authapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.remote.AuthApi
import com.example.authapp.remote.RetrofitClient
import com.example.authapp.repository.AuthRepository

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val api: AuthApi = RetrofitClient.instance.create(AuthApi::class.java)

            val repository = AuthRepository(api, context.applicationContext)

            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}