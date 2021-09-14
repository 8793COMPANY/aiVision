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

}