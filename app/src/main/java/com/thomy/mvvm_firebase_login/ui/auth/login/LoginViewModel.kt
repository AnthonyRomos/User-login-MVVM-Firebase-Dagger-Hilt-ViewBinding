package com.thomy.mvvm_firebase_login.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thomy.mvvm_firebase_login.repository.AuthRepository
import com.thomy.mvvm_firebase_login.utils.UiState
import com.thomy.mvvm_firebase_login.data.UserLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(
    private val repository: AuthRepository
) : ViewModel(){

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }



    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState>
        get() = _viewState

    fun login(
        email: String,
        password: String
    ) {
        _login.value = UiState.Loading
        repository.loginUser(
            email,
            password
        ) {
            _login.value = it
        }
    }
    fun onFieldsChanged(email: String, password: String) {
        _viewState.value = LoginViewState(
            isValidEmail = isValidEmail(email),
            isValidPassword = isValidPassword(password)
        )
    }

    private fun isValidEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidPassword(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()


    fun getSession(result: (UserLogin?) -> Unit) {
        repository.getSession(result)
    }
}