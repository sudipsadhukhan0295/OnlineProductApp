package com.mcsadhukhan.app.util

import android.content.Context
import com.google.gson.Gson
import com.mcsadhukhan.app.BuildConfig
import java.io.Serializable

object SharedPreferenceHelper {

    const val prefFileName = BuildConfig.APPLICATION_ID + ".pref"

    /**
     * Set a string shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */

    fun setString(context: Context, key: String, value: String) {
        val settings = context.getSharedPreferences(prefFileName, 0)
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Set a integer shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    fun setInt(context: Context, key: String, value: Int) {
        val settings = context.getSharedPreferences(prefFileName, 0)
        val editor = settings.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * Set a integer shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    fun setObject(context: Context, key: String, value: Serializable) {
        val settings = context.getSharedPreferences(prefFileName, 0)
        val editor = settings.edit()
        editor.putString(key, Gson().toJson(value))
        editor.apply()
    }

    /**
     * Set a Boolean shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    fun setBoolean(context: Context, key: String, value: Boolean) {
        val settings = context.getSharedPreferences(prefFileName, 0)
        val editor = settings.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun clear(context: Context){
        val settings = context.getSharedPreferences(prefFileName, 0)
        val editor = settings.edit()
        editor.clear().apply()
    }

    /**
     * Get a string shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    fun getString(context: Context, key: String, defValue: String): String? {
        val settings = context.getSharedPreferences(prefFileName, 0)
        return settings.getString(key, defValue)
    }

    /**
     * Get a integer shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    fun getInt(context: Context, key: String, defValue: Int): Int {
        val settings = context.getSharedPreferences(prefFileName, 0)
        return settings.getInt(key, defValue)
    }

    /**
     * Get a boolean shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        val settings = context.getSharedPreferences(prefFileName, 0)
        return settings.getBoolean(key, defValue)
    }

    fun <Y: Serializable> getObject(context: Context, key: String, type: Class<Y>): Y{
        val settings = context.getSharedPreferences(prefFileName, 0)
        val json = settings.getString(key, "")
        return Gson().fromJson(json, type)

    }
}