package com.thomy.mvvm_firebase_login.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.thomy.mvvm_firebase_login.data.UserProfile
import com.thomy.mvvm_firebase_login.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel(){

    private val _userProfileLiveData = MutableLiveData<UserProfile>()
    val userProfileLiveData: LiveData<UserProfile> = _userProfileLiveData

    fun loadUserProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userProfile = UserProfile(currentUser.email)
            _userProfileLiveData.postValue(userProfile)
        }
    }

    fun logout(result: () -> Unit) {
        repository.logout(result)
    }

}