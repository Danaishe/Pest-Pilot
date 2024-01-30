package com.hansoft.lepidopteran

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.hansoft.lepidopteran.databinding.ActivitySplashBinding
import com.hansoft.lepidopteran.helpers.Util

@Suppress("DEPRECATION")
class splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val util = Util()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.greenColor)

        if (util.getLocalData(this,"mode") == "dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        util.saveLocalData(this,"d","1")
        util.saveLocalData(this,"add","0")

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)

    }
}