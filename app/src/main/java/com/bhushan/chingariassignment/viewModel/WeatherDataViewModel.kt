package com.bhushan.chingariassignment.viewModel

import androidx.lifecycle.ViewModel
import com.bhushan.chingariassignment.network.WeatherDataRepository

class WeatherDataViewModel(private val weatherDataRepository: WeatherDataRepository) : ViewModel() {
    fun getWeatherData(lat: Double, long: Double) = weatherDataRepository.getWeatherData(lat, long)
}