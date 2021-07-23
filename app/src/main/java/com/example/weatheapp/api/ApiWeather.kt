package com.example.weatheapp.api

import com.example.weatheapp.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiWeather {


        @GET("data/2.5/weather?")
        suspend fun getCurrentWeatherData1(@Query("lat") lat: String, @Query("lon") lon: String, @Query("APPID") app_id: String) : Response<WeatherResponse>
}