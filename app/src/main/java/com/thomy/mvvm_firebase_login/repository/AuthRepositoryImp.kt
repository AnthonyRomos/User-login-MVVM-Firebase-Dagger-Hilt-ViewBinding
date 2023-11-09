package com.thomy.mvvm_firebase_login.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.thomy.mvvm_firebase_login.data.UserLogin
import com.thomy.mvvm_firebase_login.data.UserRegister
import com.thomy.mvvm_firebase_login.utils.FireStoreCollection
import com.thomy.mvvm_firebase_login.utils.SharedPrefConstants
import com.thomy.mvvm_firebase_login.utils.UiState

class AuthRepositoryImp(
    val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val appPreferences: SharedPreferences,
    private val gson: Gson
) : AuthRepository {

    override fun registerUser(
        email: String,
        password: String,
        user: UserRegister, result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    user.id = it.result.user?.uid ?: ""
                    updateUserInfo(user) { state ->
                        when (state) {
                            is UiState.Success -> {
                                storeSession(id = it.result.user?.uid ?: "") {
                                    if (it == null) {
                                        result.invoke(UiState.Failure("UserLogin register successfully but session failed to store"))
                                    } else {
                                        result.invoke(
                                            UiState.Success("Usuario regitrado satisfactoriamente")
                                        )
                                    }
                                }
                            }

                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }

                            else -> {}
                        }
                    }
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }


    override fun updateUserInfo(user: UserRegister, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        document
            .set(user)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("UserLogin has been update successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun loginUser(
        email: String,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeSession(id = task.result.user?.uid ?: "") {
                        if (it == null) {
                            result.invoke(UiState.Failure("Failed to store local session"))
                        } else {
                            result.invoke(UiState.Success("Has iniciado sesion correctamente"))
                        }
                    }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Lo sentimos, verifica email y contraseña"))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Email has been sent"))

                } else {
                    result.invoke(UiState.Failure(task.exception?.message))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, Check email"))
            }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, null).apply()
        result.invoke()
    }

    override fun storeSession(id: String, result: (UserLogin?) -> Unit) {
        database.collection(FireStoreCollection.USER).document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(UserLogin::class.java)
                    appPreferences.edit()
                        .putString(SharedPrefConstants.USER_SESSION, gson.toJson(user)).apply()
                    result.invoke(user)
                } else {
                    result.invoke(null)
                }
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }

    override fun getSession(result: (UserLogin?) -> Unit) {
        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION, null)
        if (userStr == null) {
            result.invoke(null)
        } else {
            val user = gson.fromJson(userStr, UserLogin::class.java)
            result.invoke(user)
        }
    }
}