package com.bhushan.chingariassignment.network

import androidx.lifecycle.LiveData
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.utils.networkUtility.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService  {
    @GET("data/2.5/weather")
    fun getWeatherData(@Query("lat") lat: Double,
                       @Query("lon") lon: Double,
                       @Query("appid") api_key: String): LiveData<ApiResponse<WeatherData>>
}