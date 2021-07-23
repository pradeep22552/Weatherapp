package com.example.weatheapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.weatheapp.Config
import com.example.weatheapp.R

import com.example.weatheapp.model.loc
import com.example.weatheapp.utils.LocationHelper
import com.example.weatheapp.utils.Resultres
import com.example.weatheapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: com.example.weatheapp.databinding.ActivityMainBinding
    private lateinit var currentLocation: Location
    private lateinit var locationManager: LocationManager

    val PERMISSION_ID = 42
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var mLastLocation: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        subscribeUi()
        //       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }
    private fun showError(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

    }
    private fun subscribeUi() {
// init ViewModel
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.weather
                .observe(this, Observer { result ->
                    when (result.status) {
                        Resultres.Status.SUCCESS -> {
                            result.data?.let { list ->
                                val iconCode = list.icon?.replace("n", "d")
                                Toast.makeText(applicationContext, list.toString(), Toast.LENGTH_SHORT).show()
                                Glide.with(applicationContext).load(Config.IMG_URL + "${iconCode}@4x.png").into(binding.imageWeatherHumanReaction)
                                binding.textCityName.text = list.cityName
                                binding.textLabelDegree.text = list.countryName
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



}






