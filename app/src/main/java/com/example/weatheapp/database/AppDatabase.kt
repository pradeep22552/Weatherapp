package com.example.radioplayer.database



import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatheapp.model.WeatherDetail


@Database(entities = [WeatherDetail::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun Weatherdao(): WeatherDAO
}