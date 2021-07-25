package com.example.weatheapp.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.weatheapp.Config
import com.example.weatheapp.R

import com.example.weatheapp.model.Locdet
import com.example.weatheapp.utils.PermissionUtils
import com.example.weatheapp.utils.Resultres
import com.example.weatheapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity  : AppCompatActivity(){


    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding:com.example.weatheapp.databinding.ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    private fun showError(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

    }
    private fun subscribeUi(lat:String,lon:String) {
// init ViewModel
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
       var c= Locdet()
        c.lat=lat
        c.lon=lon
        weatherViewModel.fetchWeather(c)
        weatherViewModel.weather
                .observe(this, Observer { result ->
                    when (result.status) {
                        Resultres.Status.SUCCESS -> {
                            result.data?.let { list ->
                                val iconCode = list.icon?.replace("n", "d")
                                //Toast.makeText(applicationContext, list.toString(), Toast.LENGTH_SHORT).show()
                                Glide.with(applicationContext).load(Config.IMG_URL + "${iconCode}@4x.png").into(binding.imageWeatherHumanReaction)
                                binding.textCityName.text = list.cityName

                                binding.textTemperature.text = list.temp.toString()
                                binding.textTodaysDate.text=list.dateTime.toString()

                            }
                        }
                        // loading.visibility = View.GONE
                        Resultres.Status.ERROR -> {
                            result.message?.let {
                                showError(it)
                            }
                            // loading.visibility = View.GONE
                        }
                        Resultres.Status.LOADING -> {
                            // loading.visibility = View.VISIBLE
                        }
                    }

                })
    }



    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
            maxWaitTime= 100
        }

        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                          subscribeUi(location.latitude.toString(),location.longitude.toString())
                        }
                        // Few more things we can do here:
                        // For example: Update the location of user on server
                    }
                },
                Looper.myLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                            this,
                            getString(R.string.location_permission_not_granted),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

}





