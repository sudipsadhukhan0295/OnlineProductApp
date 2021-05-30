package com.mcsadhukhan.app.util

import java.text.SimpleDateFormat
import java.util.*


fun Calendar.convertStringFormat(stringFormat: String): String {
    val df = SimpleDateFormat(stringFormat, Locale.US)
    return df.format(this.time)
}