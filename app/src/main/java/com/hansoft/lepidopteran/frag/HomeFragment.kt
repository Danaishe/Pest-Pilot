package com.hansoft.lepidopteran.frag

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.databinding.FragmentHomeBinding
import com.hansoft.lepidopteran.helpers.Constants
import com.hansoft.lepidopteran.views.AddChemical
import com.hansoft.lepidopteran.views.AddTracker
import com.hansoft.lepidopteran.views.Addpests
import com.hansoft.lepidopteran.weatherhelper.ApiInterface
import com.hansoft.lepidopteran.weatherhelper.ApiUtilities
import com.hansoft.lepidopteran.weatherhelper.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.Locale

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    private var result : WeatherModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        callApi()

        binding.pest.setOnClickListener {
            startActivity(Intent(requireContext(), Addpests::class.java))
        }
        binding.chemical.setOnClickListener {
            startActivity(Intent(requireContext(), AddChemical::class.java))
        }

        binding.tracker.setOnClickListener {
            startActivity(Intent(requireContext(), AddTracker::class.java))
        }

        return binding.root
    }

    private fun callApi() {
        var city = ""
        val weatherApi = ApiUtilities.getInstance().create(ApiInterface::class.java)
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
                city = addresses[0].locality
                fetchWeatherData(weatherApi, city)
            }
        }.addOnFailureListener {
            fetchWeatherData(weatherApi,city)
        }
    }

    private fun fetchWeatherData(
        weatherApi: ApiInterface,
        city: String,
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = weatherApi.getCityWeather(city, Constants.apiKey)
                if (response.isSuccessful) {
                    val result = response.body()
                    requireActivity().runOnUiThread {
                        if (result != null) {
                            updateUI(result)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun updateUI(body : WeatherModel?) {
        Log.d("123", "result.toString()")
        binding.pressure.text = "Pressure "+body!!.main.temp.toString()+" hPa"
        binding.tem.text = "Temperature "+k2c(body.main.temp).toString()
        binding.humidity.text = "Humidity "+body.main.humidity.toString()+"%"
        binding.apply {
            when (body.weather[0].id) {
                //Thunderstorm
                in 200..232 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_storm_weather))
                }
                //Drizzle
                in 300..321 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_few_clouds))
                }
                //Rain
                in 500..531 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_rainy_weather))
                }
                //Snow
                in 600..622 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_snow_weather))
                }
                //Atmosphere
                in 701..781 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_broken_clouds))
                }
                //Clear
                800 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_clear_day))
                }
                //Clouds
                in 801..804 -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_cloudy_weather))
                }
                //unknown
                else -> {
                    binding.weatherimg.setImageDrawable(resources.getDrawable(R.drawable.ic_unknown))
                }
            }
        }
    }

    private fun k2c(t:Double):Double{
        var intTemp=t
        intTemp=intTemp.minus(273)
        return intTemp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
    }

}