package com.bhushan.chingariassignment.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhushan.chingariassignment.data.WeatherData

@Database(
    entities = [
        WeatherData::class
    ],
    version = 1
)
@TypeConverters(
    WeatherDataTypeConverter::class
)

abstract class WeatherDataRoomDatabase : RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDataDao
}