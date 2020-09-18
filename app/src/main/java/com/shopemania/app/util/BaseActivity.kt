package com.shopemania.app.util

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.shopemania.app.R


open class BaseActivity : AppCompatActivity() {

    private fun createSnackbar(view: View, msg: String):Snackbar{
        return Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
    }

    fun showSnackbar(view: View, msg: String) {
        createSnackbar(view,msg).show()
    }

    fun errorSnackbar(view: View){
        createSnackbar(view,"Something went wrong!").show()
    }

    fun showSnackbar(view: View, mContext: Context, msg: String) {
        val snackbar = createSnackbar(view, msg)
        snackbar.setActionTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
        snackbar.setAction("OK") { snackbar.dismiss() }
        snackbar.show()
    }

}