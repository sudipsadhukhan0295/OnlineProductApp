package com.mcsadhukhan.app.signup

import android.app.Activity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emi.manager.network.ApiResponse
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.mcsadhukhan.app.model.User
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpRepository: SignUpRepository,
    private val activity: Activity
) : ViewModel() {
    var userLiveData = MediatorLiveData<ApiResponse<FirebaseUser>>()

    init {
        userLiveData.addSource(signUpRepository.userLiveData) { response ->
            userLiveData.value = response
        }
    }

    fun sendOtp(phoneNumber: String): MutableLiveData<ApiResponse<String>>? {

        return signUpRepository.sendOtp(phoneNumber, activity)
    }

    fun login(credential: AuthCredential) {

        signUpRepository.signInWithPhoneAuthCredential(credential, activity)
    }

    fun linkGoogleAuth(credential: AuthCredential): MutableLiveData<ApiResponse<Boolean>> {
        val data = MutableLiveData<ApiResponse<Boolean>>()
        viewModelScope.launch {
            signUpRepository.linkGoogleAuth(credential).let { result ->
                data.value = result
            }
        }
        return data
    }

    fun storeUserDetails(uid: String, userDetail: User): MutableLiveData<ApiResponse<Boolean>> {
        val data = MutableLiveData<ApiResponse<Boolean>>()
        viewModelScope.launch {
            signUpRepository.storeUserDetails(uid, userDetail,activity).let { result ->
                data.value = result
            }
        }
        return data
    }

}