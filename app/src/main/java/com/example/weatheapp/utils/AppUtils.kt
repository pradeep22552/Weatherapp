package com.example.weatheapp.utils

import android.annotation.SuppressLint
import com.example.weatheapp.Config
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {


    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateTime(dateFormat: String): String =
            SimpleDateFormat(dateFormat).format(Date())

    @SuppressLint("SimpleDateFormat")
    fun isTimeExpired(dateTimeSavedWeather: String?): Boolean {
        dateTimeSavedWeather?.let {
            val currentDateTime = Date()
            val savedWeatherDateTime =
                    SimpleDateFormat(Config.DATE_FORMAT_1).parse(it)
            val diff: Long = currentDateTime.time - savedWeatherDateTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            if (minutes > 120)
                return true
        }
        return false
    }

}