package com.thomy.mvvm_firebase_login.ui.auth.login

data class LoginViewState(
    val isLoading: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true

)