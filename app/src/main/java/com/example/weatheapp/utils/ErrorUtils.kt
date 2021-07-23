package com.example.weatheapp.utils


import com.example.weatheapp.model.Errorres
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


/**
 * parses error response body
 */
object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): Errorres? {
        val converter = retrofit.responseBodyConverter<Errorres>(Errorres::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Errorres()
        }
    }
}