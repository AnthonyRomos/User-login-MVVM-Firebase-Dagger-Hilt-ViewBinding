package com.thomy.mvvm_firebase_login.repository

import com.thomy.mvvm_firebase_login.utils.UiState
import com.thomy.mvvm_firebase_login.data.UserLogin
import com.thomy.mvvm_firebase_login.data.UserProfile
import com.thomy.mvvm_firebase_login.data.UserRegister

interface AuthRepository {
    fun registerUser(email: String, password: String, user: UserRegister, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: UserRegister, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun storeSession(id: String, result: (UserLogin?) -> Unit)
    fun getSession(result: (UserLogin?) -> Unit)
}