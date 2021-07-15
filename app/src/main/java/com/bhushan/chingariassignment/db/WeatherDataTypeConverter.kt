package com.bhushan.chingariassignment.db

import androidx.room.TypeConverter
import com.bhushan.chingariassignment.data.WeatherMainData
import com.bhushan.chingariassignment.data.WeatherWindData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherDataTypeConverter {
    @TypeConverter
    fun fromWeatherMainData(weatherMainData: WeatherMainData?): String? {
        val gson = Gson()
        return gson.toJson(weatherMainData)
    }

    @TypeConverter
    fun fromStringToWeatherMainData(value: String?): WeatherMainData? {
        val listType =
            object : TypeToken<WeatherMainData?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromWeatherWindData(weatherWindData: WeatherWindData?): String? {
        val gson = Gson()
        return gson.toJson(weatherWindData)
    }

    @TypeConverter
    fun fromStringToWeatherWindData(value: String?): WeatherWindData? {
        val listType =
            object : TypeToken<WeatherWindData?>() {}.type
        return Gson().fromJson(value, listType)
    }
}