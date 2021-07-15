package com.bhushan.chingariassignment.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherData(
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "main")
        var main: WeatherMainData? = null,
        @ColumnInfo(name = "wind")
        var wind: WeatherWindData? = null,
        @PrimaryKey
        @ColumnInfo(name = "dt")
        var dt: Long = 0L,
)
