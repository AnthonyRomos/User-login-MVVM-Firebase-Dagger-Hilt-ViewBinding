package com.thomy.mvvm_firebase_login.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.thomy.mvvm_firebase_login.R
import com.thomy.mvvm_firebase_login.databinding.FragmentLoginBinding
import com.thomy.mvvm_firebase_login.utils.UiState
import com.thomy.mvvm_firebase_login.utils.dismissKeyboard
import com.thomy.mvvm_firebase_login.utils.hide
import com.thomy.mvvm_firebase_login.utils.isValidEmail
import com.thomy.mvvm_firebase_login.utils.loseFocusAfterAction
import com.thomy.mvvm_firebase_login.utils.onTextChanged
import com.thomy.mvvm_firebase_login.utils.show
import com.thomy.mvvm_firebase_login.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val TAG: String = "LoginFragment"
    lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()


        binding.etEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.etEmail.onTextChanged { onFieldChanged() }

        binding.etPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
        binding.etPassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.etPassword.onTextChanged { onFieldChanged() }

        binding.btnLogin.setOnClickListener {
            it.dismissKeyboard()
            if (validation()) {
                loginViewModel.login(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                )
            }
        }
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }
    }

    private fun observer() {
        loginViewModel.login.observe(viewLifecycleOwner) { state ->

            when (state) {
                is UiState.Loading -> {
                    binding.btnLogin.setText("")
                    binding.progressBar.show()
                }

                is UiState.Failure -> {
                    binding.btnLogin.setText("Login")
                    binding.progressBar.hide()
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.btnLogin.setText("Login")
                    binding.progressBar.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_home_navigation)
                }
            }
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        validation()
        if (!hasFocus) {
            loginViewModel.onFieldsChanged(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || !email.isValidEmail()) {
            isValid = false
            binding.tilEmail.error = getString(R.string.login_error_mail)
        } else {
            binding.tilEmail.error = null
        }
        if (password.isEmpty() || password.length < 8) {
            isValid = false
            binding.tilPassword.error = getString(R.string.login_error_password)
        } else {
            binding.tilPassword.error = null
        }
        return isValid
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.getSession { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_loginFragment_to_home_navigation)
            }
        }
    }
}

