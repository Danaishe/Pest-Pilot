package com.hansoft.lepidopteran.weatherhelper

import com.hansoft.lepidopteran.helpers.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {
    fun getInstance() : Retrofit {
        return Retrofit.Builder().baseUrl(Constants.baseUrl).
        addConverterFactory(GsonConverterFactory.create()).build()
    }
}