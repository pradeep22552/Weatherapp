package com.example.weatheapp.utils


import com.example.weatheapp.model.Errorres

data class Resultres<out T>(val status: Status, val data: T?, val error: Errorres?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Resultres<T> {
            return Resultres(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: Errorres?): Resultres<T> {
            return Resultres(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): Resultres<T> {
            return Resultres(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}