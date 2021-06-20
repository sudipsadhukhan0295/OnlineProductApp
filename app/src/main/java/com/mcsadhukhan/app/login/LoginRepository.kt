package com.mcsadhukhan.app.login

import android.app.Activity
import android.util.Log
import com.mcsadhukhan.app.network.ApiResponse
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.messaging.FirebaseMessaging
import com.mcsadhukhan.app.util.ConstantHelper
import com.mcsadhukhan.app.util.FunctionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class LoginRepository {

    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    suspend fun checkIfEmailExist(socialMediaId: String): ApiResponse<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val userQuerySnapshot =
                    db.collection("users").whereEqualTo("socialMediaId", socialMediaId)
                        .get(Source.SERVER).await()
                val isNewUser = userQuerySnapshot.documents.isEmpty()
                return@withContext ApiResponse(isNewUser)

            } catch (e: Exception) {
                return@withContext ApiResponse<Boolean>(e)
            }
        }
    }

    suspend fun getGmailLogin(
        credential: AuthCredential,
        activity: Activity
    ): ApiResponse<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val signIn = firebaseAuth.signInWithCredential(credential).await()
                val userEmail = signIn.user?.email
                val isUserLogin = !userEmail.isNullOrEmpty()
                val deviceToken = getDeviceToken()
                if (deviceToken.responseBody == null) return@withContext ApiResponse<Boolean>(
                    deviceToken.exception
                )
                val result = updateDeviceToken(deviceToken.responseBody, activity)
                if (result.exception != null) return@withContext ApiResponse<Boolean>(result.exception)
                return@withContext ApiResponse(isUserLogin)
            } catch (e: Exception) {
                return@withContext ApiResponse<Boolean>(e)
            }
        }
    }

    suspend fun getDeviceToken(): ApiResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                val result = FirebaseMessaging.getInstance().token.await()
                Log.e("TOKEN",result)
                return@withContext ApiResponse(result)

            } catch (e: Exception) {
                return@withContext ApiResponse<String>(e)
            }
        }
    }

    private suspend fun updateDeviceToken(
        deviceToken: String?,
        activity: Activity
    ): ApiResponse<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = firebaseAuth.currentUser?.uid
                val deviceId: String? = FunctionHelper.getDeviceId(activity)
                val deviceTokenMap = HashMap<String, String>()
                val deviceLastLoginHashMap = HashMap<String, Date>()
                if (deviceId != null && deviceToken != null && userId != null) {
                    deviceLastLoginHashMap[deviceId] = FunctionHelper.currentDateTime!!
                    deviceTokenMap[deviceId] = deviceToken
                    val data = hashMapOf(
                        ConstantHelper.PARAM_DEVICE_TOKEN to deviceTokenMap,
                        ConstantHelper.PARAM_DEVICE_LAST_LOGIN_TIME to deviceLastLoginHashMap
                    )

                    db.collection(ConstantHelper.COLLECTION_USERS).document(userId).set(
                        data,
                        SetOptions.merge()
                    ).await()
                    return@withContext ApiResponse(true)
                }
                return@withContext ApiResponse(false)
            } catch (e: Exception) {
                return@withContext ApiResponse<Boolean>(e)
            }
        }
    }
}