package com.bhushan.chingariassignment.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.utils.dbUtility.DatabaseHelper
import com.bhushan.chingariassignment.utils.networkUtility.ApiResponse
import com.bhushan.chingariassignment.utils.networkUtility.AppExecutors
import com.bhushan.chingariassignment.utils.networkUtility.NetworkBoundResource
import com.bhushan.chingariassignment.utils.networkUtility.Resource

/**
 * Repository responsible to do n/w & room db operations
 * then return data to caller
 */

private const val API_KEY = "adf7f3e50e2b8cc95842c4e07c8208c8"

class WeatherDataRepository constructor(
    private val appExecutors: AppExecutors,
    private val weatherService: WeatherService
) {

    fun getWeatherData(latVal: Double, longVal: Double): LiveData<Resource<List<WeatherData>>> {
        return object : NetworkBoundResource<List<WeatherData>, WeatherData>(appExecutors) {
            override fun saveCallResult(item: WeatherData) {
                DatabaseHelper.insertWeatherData(item)
            }

            override fun shouldFetch(data: List<WeatherData>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<WeatherData>> {
                return MutableLiveData(DatabaseHelper.getWeatherDataList())
            }

            override fun createCall(): LiveData<ApiResponse<WeatherData>> {
                return weatherService.getWeatherData(
                    lat = latVal,
                    lon = longVal,
                    api_key = API_KEY
                )
            }

        }.asLiveData()
    }
}