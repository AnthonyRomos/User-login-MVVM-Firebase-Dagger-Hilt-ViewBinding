package com.thomy.mvvm_firebase_login.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thomy.mvvm_firebase_login.repository.AuthRepository
import com.thomy.mvvm_firebase_login.utils.UiState
import com.thomy.mvvm_firebase_login.data.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _viewState = MutableStateFlow(RegisterViewState())
    val viewState: StateFlow<RegisterViewState>
        get() = _viewState


    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword


    fun register(
        email: String,
        password: String,
        userRegister: UserRegister
    ) {
        _register.value = UiState.Loading
        repository.registerUser(
            email = email,
            password = password,
            user = userRegister
        ) { _register.value = it }
    }


    fun onFieldsChanged(userRegister: UserRegister) {
        _viewState.value = userRegister.toRegisterViewState()
    }

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String, passwordConfirmation: String): Boolean =
        (password.length >= MIN_PASSWORD_LENGTH) || password.isEmpty()


    private fun isValidOrEmptyPasswordConfirmation(passwordConfirmation: String): Boolean =
        (passwordConfirmation.length >= MIN_PASSWORD_LENGTH) || passwordConfirmation.isEmpty()


    private fun UserRegister.toRegisterViewState(): RegisterViewState {
        return RegisterViewState(
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password, passwordConfirmation)
        )
    }


    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loading
        repository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }
}