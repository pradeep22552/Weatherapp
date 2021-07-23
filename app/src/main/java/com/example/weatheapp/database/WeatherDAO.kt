package com.example.radioplayer.database


import androidx.room.*
import com.example.weatheapp.model.WeatherDetail


@Dao
interface WeatherDAO {

 @Query("SELECT * FROM weather_detail")
    fun getAll(): WeatherDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(weatherall: WeatherDetail)

    @Delete
    fun deleteAll(weatherdelall:WeatherDetail)
}
