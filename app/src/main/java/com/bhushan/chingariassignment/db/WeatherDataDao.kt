package com.bhushan.chingariassignment.db

import androidx.room.*
import com.bhushan.chingariassignment.data.WeatherData

@Dao
interface WeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(weatherData: WeatherData)

    @Query("SELECT * FROM WeatherData ORDER BY dt ASC")
    fun getWeatherDataList(): List<WeatherData>

    @Query("DELETE FROM WeatherData")
    fun deleteAllWeatherData()
}