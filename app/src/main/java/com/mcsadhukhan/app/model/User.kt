package com.mcsadhukhan.app.model

import com.google.firebase.firestore.Exclude
import com.google.gson.Gson
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

data class User(
    var name: String? = null,
    var emailId: String? = null,
    var photoUrl: String? = "",
    var socialMediaId: String? = null,
    var phoneNumber: String? = "",
    var deviceToken: HashMap<String, String> = HashMap(),
    var deviceLastLoginTime: HashMap<String, Date> = HashMap(),
    @get:Exclude
    var gmailIdToken: String? = "",
    @get:Exclude
    var uid: String? = ""
) : Serializable {
    override fun toString(): String {
        val gson = Gson()
        val json = gson.toJson(this)
        return json.toString()
    }
}
