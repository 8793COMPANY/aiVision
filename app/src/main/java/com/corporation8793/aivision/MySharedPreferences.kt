package com.corporation8793.aivision

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("myCourseList", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }


    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean{
        return prefs.getBoolean(key, defValue)
    }
    fun setBoolean(key: String, str: Boolean) {
        prefs.edit().putBoolean(key, str).apply()
    }

}