package com.bhushan.chingariassignment.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bhushan.chingariassignment.R
import com.bhushan.chingariassignment.utils.networkUtility.Resource
import com.bhushan.chingariassignment.utils.networkUtility.Status
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * observeOnce extension helps to remove observer attached to LiveData for Status.SUCCESS|Status.ERROR,
 * to prevent duplicate onChanged call
 */
fun <T> LiveData<Resource<T>>.observeOnce(
    lifecycleOwner: LifecycleOwner,
    observer: Observer<Resource<T>>
) {
    observe(lifecycleOwner, object : Observer<Resource<T>> {
        override fun onChanged(t: Resource<T>?) {
            observer.onChanged(t)
            if (t?.status == Status.SUCCESS || t?.status == Status.ERROR) {
                removeObserver(this)
            }
        }
    })
}

/**
 * Below methods used by WeatherDataAdapter to set formatted values for hour, temp, humidity, speed.
 * ------------------------------------------
 * getHour | getTemp | getHumidity | getSpeed
 * ------------------------------------------
 */

@SuppressLint("SimpleDateFormat")
fun getHour(time: Long): String {
    val date = Date(time)
    val format: DateFormat = SimpleDateFormat("HH:mm:ss")
    format.timeZone = TimeZone.getTimeZone("Etc/UTC")
    return format.format(date)
}

fun getTemp(context: Context, temp: Double): String {
    val currentTemp = temp - 273.15
    return context.resources.getString(R.string.temp, currentTemp.toString())
}

fun getHumidity(context: Context, humidity: Double): String {
    return context.resources.getString(R.string.percentage, humidity.toString())
}

fun getSpeed(context: Context, speed: Double): String {
    return context.resources.getString(R.string.speed, speed.toString())
}