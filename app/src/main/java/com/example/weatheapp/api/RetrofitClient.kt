package com.example.weatheapp.api


import androidx.databinding.library.BuildConfig
import com.example.weatheapp.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor.Level

object RetrofitClient {


    val retrofitClient: Retrofit.Builder by lazy {

        val levelType: Level
        if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            levelType = Level.BODY else levelType = Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiWeather by lazy {
        retrofitClient
            .build()
            .create(ApiWeather::class.java)
    }
}