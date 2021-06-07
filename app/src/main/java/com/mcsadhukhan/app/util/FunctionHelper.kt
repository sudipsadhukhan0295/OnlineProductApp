package com.mcsadhukhan.app.util

import android.content.Context
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ANDROID_ID
import java.text.SimpleDateFormat
import java.util.*

object FunctionHelper {
    fun getDeviceId(context: Context): String? {
        return Secure.getString(context.contentResolver, ANDROID_ID)
    }

    val currentDateTime: Date? = Calendar.getInstance().time

    fun getFormattedDate(date: Date?):String?{
        if (date!=null) {
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())
            return simpleDateFormat.format(date)
        }
        return null
    }
}