package com.example.weatheapp.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.model.Locdet
import com.example.weatheapp.model.WeatherDetail
import com.example.weatheapp.repository.WeatherRepository
import com.example.weatheapp.utils.Resultres
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class WeatherViewModel@ViewModelInject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {
    private val WeatherList = MutableLiveData<Resultres<WeatherDetail>>()
    val weather= WeatherList

    fun fetchWeather(locdet: Locdet) {
        viewModelScope.launch(){
            weatherRepository.fetchweather(locdet.lat.toString(),locdet.lon.toString()).collect {
                WeatherList.value =it
           }
        }
    }




}