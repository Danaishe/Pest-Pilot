package com.hansoft.lepidopteran.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.database.trackerdatabase.Tracker
import com.hansoft.lepidopteran.database.trackerdatabase.TrackerViewModel
import com.hansoft.lepidopteran.databinding.ActivityAddTrackerBinding

class AddTracker : AppCompatActivity() {
    private lateinit var binding: ActivityAddTrackerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.greenColor)

        binding.date.setOnClickListener {
            showDatePickerDialog()
        }

        binding.save.setOnClickListener {
            if(binding.chemical.text.isEmpty() || binding.target.text.isEmpty() ||
                binding.area.text.isEmpty() || binding.effectiveness.text.toString() == "" ||
                binding.actualdate.text == "Select Date"){
                Toast.makeText(this,"Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val trackerViewModel = ViewModelProvider(this).get(TrackerViewModel::class.java)
                val t = Tracker(
                    0, chemical = binding.chemical.text.toString() , target = binding.target.text.toString(),
                    area = binding.area.text.toString() , effectiveness = binding.effectiveness.text.toString(),
                    date = binding.actualdate.text.toString()
                )
                trackerViewModel.insert(t)
                finish()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                binding.actualdate.text = "$year-${month + 1}-$dayOfMonth"
            },
            2023, 0, 1 // Year, month (0-based), day
        )
        datePicker.show()
    }

}