package com.bhushan.chingariassignment

import android.content.Context

object SharedPrefHelper {
    private const val CURRENT_TEMP = "current_temp"
    private const val SHARED_PREF = "shared_pref"

    fun addCurrentTemp(context: Context, currentTemp: Double) {
        val tempInCelcius = currentTemp - 273.15
        val preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        preferences.edit().putString(CURRENT_TEMP, tempInCelcius.toString()).apply()
    }

    fun getCurrentTemp(context: Context): String {
        val preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return  preferences.getString(CURRENT_TEMP, "0")?:"0"
    }
}