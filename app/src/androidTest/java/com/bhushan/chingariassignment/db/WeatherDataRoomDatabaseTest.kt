package com.bhushan.chingariassignment.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.data.WeatherMainData
import com.bhushan.chingariassignment.data.WeatherWindData
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDataRoomDatabaseTest : TestCase() {

    private lateinit var weatherDataDao: WeatherDataDao
    private lateinit var db: WeatherDataRoomDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, WeatherDataRoomDatabase::class.java
        ).build()
        weatherDataDao = db.weatherDataDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadSpend() = runBlocking {
        val weatherMainData = WeatherMainData(22.0, 223.0)
        val weatherWindData = WeatherWindData(225.0)
        val weatherData1 = WeatherData(main = weatherMainData, wind = weatherWindData, dt = 134767, id = 1)
        weatherDataDao.insertWeatherData(weatherData1)
        val weatherData2 = WeatherData(main = weatherMainData, wind = weatherWindData, dt = 134667, id = 2)
        weatherDataDao.insertWeatherData(weatherData2)
        val weatherData3 = WeatherData(main = weatherMainData, wind = weatherWindData, dt = 134567, id = 3)
        weatherDataDao.insertWeatherData(weatherData3)
        val weatherData = weatherDataDao.getWeatherDataList()[0]
        assert(weatherData.dt == weatherData3.dt)
    }
}