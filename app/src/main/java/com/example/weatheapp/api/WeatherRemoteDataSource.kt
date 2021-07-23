package com.example.weatheapp.api


import androidx.lifecycle.MutableLiveData
import com.example.weatheapp.Config
import com.example.weatheapp.model.WeatherDetail
import com.example.weatheapp.model.WeatherResponse
import com.example.weatheapp.model.loc

import com.example.weatheapp.utils.ErrorUtils
import com.example.weatheapp.utils.Resultres
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {
    private val loc =loc()

    suspend fun fetchweather(): Resultres<WeatherResponse> {
        val weatherService = retrofit.create(ApiWeather::class.java);
        return getResponse(
                //request = { weatherService.getCurrentWeatherData1(loc.lat, loc.lon, Config.API_KEY) },
            request = { weatherService.getCurrentWeatherData1("13.52", "79.82", Config.API_KEY) },
                defaultErrorMessage = "Error fetching weather details")
    }


    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Resultres<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Resultres.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Resultres.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Resultres.error("Unknown Error", null)
        }
    }
}