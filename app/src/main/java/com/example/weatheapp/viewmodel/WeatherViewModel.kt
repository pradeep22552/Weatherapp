package com.example.weatheapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.model.WeatherDetail
import com.example.weatheapp.repository.WeatherRepository
import com.example.weatheapp.utils.Resultres
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


class WeatherViewModel @ViewModelInject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val WeatherList = MutableLiveData<Resultres<WeatherDetail>>()
    val weather = WeatherList
    init {
        fetchWeather()
    }
   private fun fetchWeather() {
        viewModelScope.launch(){
            weatherRepository.fetchweather().collect {
                WeatherList.value =it
           }
        }
    }
}