package com.thomy.mvvm_firebase_login.ui.auth.register

data class RegisterViewState(

val isLoading: Boolean = false,
val isValidEmail: Boolean = true,
val isValidPassword: Boolean = true,


){
    fun userValidated() = isValidEmail && isValidPassword
}

