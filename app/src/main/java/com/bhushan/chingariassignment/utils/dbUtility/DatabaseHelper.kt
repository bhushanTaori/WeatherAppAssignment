package com.bhushan.chingariassignment.utils.dbUtility

import android.content.Context
import androidx.room.Room
import androidx.room.Transaction
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.db.WeatherDataRoomDatabase
import kotlinx.coroutines.*

/**
 * It helps to do below operations,
 * ------------------------------------------------
 *  init | insertWeatherData | getWeatherDataList
 * ------------------------------------------------
 */

object DatabaseHelper {
    private const val DB_NAME = "WeatherDataRoomDatabase"
    lateinit var roomDb: WeatherDataRoomDatabase

    fun init(context: Context) {
        roomDb = Room.databaseBuilder(
            context,
            WeatherDataRoomDatabase::class.java, DB_NAME
        ).build()
    }

    private fun getDBInstance(): WeatherDataRoomDatabase? {
        if (DatabaseHelper::roomDb.isInitialized) {
            return roomDb
        }

        throw Exception("Room Database not initialized")
    }

    @Transaction
    fun insertWeatherData(weatherData: WeatherData) {
        runBlocking {
            val job = GlobalScope.launch {
                getDBInstance()!!.weatherDataDao()
                    .insertWeatherData(weatherData)
            }
            job.join()
        }
    }

    @Transaction
    fun getWeatherDataList(): List<WeatherData>? {
        var list: List<WeatherData>? = null
        runBlocking {
            val job = GlobalScope.launch {
                list = getDBInstance()!!.weatherDataDao().getWeatherDataList()
            }
            job.join()
        }
        return list
    }
}