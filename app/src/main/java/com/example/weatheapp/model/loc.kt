package com.example.weatheapp.model

class loc () {
    var lat: String = "0"
    var lon: String = "0"
        private set

    fun locate(la: String,lo:String) {
        lat = la
        lon=lo
    }
}