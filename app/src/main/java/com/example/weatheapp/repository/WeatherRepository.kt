package com.example.weatheapp.repository

import com.example.radioplayer.database.WeatherDAO
import com.example.weatheapp.Config
import com.example.weatheapp.api.WeatherRemoteDataSource
import com.example.weatheapp.model.WeatherDetail
import com.example.weatheapp.utils.AppUtils
import com.example.weatheapp.utils.Resultres
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository  @Inject constructor(
        private val weatherDao: WeatherDAO,
        private val weather: WeatherRemoteDataSource,
    ) {
    suspend fun fetchweather(): Flow<Resultres<WeatherDetail>?> {
        return flow {
            if(fetchWeatherCached()!=null){
                fetchWeatherCached()?.data?.let{
                    if (AppUtils.isTimeExpired(it.dateTime) ){
                        //emit(Resultres.loading())
                        val result = weather.fetchweather()
                        val weatherdetail = WeatherDetail()
                        //Cache to database if response is successful
                        if (result.status == Resultres.Status.SUCCESS) {
                            result.data?.let { it ->
                                weatherDao.deleteAll(weatherdetail)
                                weatherdetail.id = it.id
                                weatherdetail.icon = it.weather.first().icon
                                weatherdetail.cityName = it.name?.toLowerCase()
                                weatherdetail.countryName = it.sys?.country
                                weatherdetail.temp = it.main?.temp
                                weatherdetail.dateTime = AppUtils.getCurrentDateTime(Config.DATE_FORMAT_1)
                                weatherDao.insertAll(weatherdetail)
                            }
                        }
                        emit(Resultres.success(weatherdetail))
                    }else{
                        emit(fetchWeatherCached())
                                          }
               }
            }
            else{
                //emit(Resultres.loading())
                val result = weather.fetchweather()
                val weatherdetail = WeatherDetail()
                //Cache to database if response is successful
                if (result.status == Resultres.Status.SUCCESS) {
                    result.data?.let { it ->
                        weatherDao.deleteAll(weatherdetail)
                        weatherdetail.id = it.id
                        weatherdetail.icon = it.weather.first().icon
                        weatherdetail.cityName = it.name?.toLowerCase()
                        weatherdetail.countryName = it.sys?.country
                        weatherdetail.temp = it.main?.temp
                        weatherdetail.dateTime = AppUtils.getCurrentDateTime(Config.DATE_FORMAT_1)
                        weatherDao.insertAll(weatherdetail)
                    }
                }
                emit(Resultres.success(weatherdetail))
            }
        }.flowOn(Dispatchers.IO)
    }
   private fun fetchWeatherCached(): Resultres<WeatherDetail>? =
      weatherDao.getAll()?.let {
          Resultres.success(it)
        }
}