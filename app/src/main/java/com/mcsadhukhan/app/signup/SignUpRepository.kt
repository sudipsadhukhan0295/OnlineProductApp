package com.mcsadhukhan.app.signup

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mcsadhukhan.app.network.ApiResponse
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.mcsadhukhan.app.App
import com.mcsadhukhan.app.login.LoginRepository
import com.mcsadhukhan.app.model.User
import com.mcsadhukhan.app.util.ConstantHelper
import com.mcsadhukhan.app.util.FunctionHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.TimeUnit

class SignUpRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val db = App.firestoreDatabase
    private val auth = FirebaseAuth.getInstance()

    val userLiveData = MutableLiveData<ApiResponse<FirebaseUser>>()

    companion object {
        const val TAG = "SignUpRepository"
    }


    fun sendOtp(email: String, activity: Activity): MutableLiveData<ApiResponse<String>> {
        val liveData = MutableLiveData<ApiResponse<String>>()
        Log.d(TAG, email)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(email, 60, TimeUnit.SECONDS, activity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "onVerificationCompleted:$credential")

                    signInWithPhoneAuthCredential(credential, activity)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w(TAG, "onVerificationFailed", e)

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        liveData.value = ApiResponse(e)
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d(TAG, "onCodeSent:$verificationId")
                    liveData.value = ApiResponse(verificationId)
                }
            }
        )
        return liveData
    }

    fun signInWithPhoneAuthCredential(credential: AuthCredential, activity: Activity) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")

                    userLiveData.value = ApiResponse(task.result?.user)
                } else {
                    userLiveData.value = ApiResponse(task.exception)

                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    suspend fun linkGoogleAuth(credential: AuthCredential): ApiResponse<Boolean> {

        return withContext(ioDispatcher) {
            try {
                auth.currentUser?.linkWithCredential(credential)?.await()
                return@withContext ApiResponse(true)
            } catch (e: Exception) {
                return@withContext ApiResponse<Boolean>(e)
            }
        }
    }

    suspend fun storeUserDetails(
        uid: String,
        userDetail: User, activity: Activity
    ): ApiResponse<Boolean> {

        return withContext(ioDispatcher) {
            try {
                val deviceId: String? = FunctionHelper.getDeviceId(activity)
                val deviceToken = LoginRepository().getDeviceToken()
                if (deviceToken.responseBody == null) return@withContext ApiResponse<Boolean>(deviceToken.exception)
                if (deviceId != null && deviceToken.responseBody != null) {
                    userDetail.deviceToken[deviceId] = deviceToken.responseBody!!
                    userDetail.deviceLastLoginTime[deviceId] = FunctionHelper.currentDateTime!!
                }
                Firebase.auth.currentUser?.updateProfile(userProfileChangeRequest { displayName = userDetail.name })?.await()

                db.collection(ConstantHelper.COLLECTION_USERS).document(uid).set(userDetail).await()
                return@withContext ApiResponse(true)
            } catch (e: Exception) {
                return@withContext ApiResponse<Boolean>(e)

            }

        }
    }
}