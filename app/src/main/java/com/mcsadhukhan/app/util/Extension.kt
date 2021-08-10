package com.mcsadhukhan.app.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*


fun Calendar.convertStringFormat(stringFormat: String): String {
    val df = SimpleDateFormat(stringFormat, Locale.US)
    return df.format(this.time)
}


fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}