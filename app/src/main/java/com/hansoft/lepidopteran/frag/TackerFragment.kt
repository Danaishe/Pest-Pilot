package com.hansoft.lepidopteran.frag

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.database.trackerdatabase.TrackerViewModel
import com.hansoft.lepidopteran.databinding.FragmentTackerBinding
import com.hansoft.lepidopteran.frag.fraghelpers.TrackerAdapter
import com.hansoft.lepidopteran.views.AddTracker

class TackerFragment : Fragment() {
    private lateinit var binding: FragmentTackerBinding
    var selectedDate = ""
    private lateinit var trackeradapter: TrackerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTackerBinding.inflate(layoutInflater, container, false)

        val iconColor = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            ContextCompat.getColor(requireContext(), R.color.black)
        } else {
            ContextCompat.getColor(requireContext(), R.color.white)
        }
        ImageViewCompat.setImageTintList(binding.fillteri, ColorStateList.valueOf(iconColor))

        binding.myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val trackerViewModel = ViewModelProvider(this)[TrackerViewModel::class.java]
        trackeradapter = TrackerAdapter(trackerViewModel)
        trackerViewModel.allTracker.observe(viewLifecycleOwner) { pests ->
            pests?.let { trackeradapter.setTracker(it) }
        }
        binding.myRecyclerView.adapter = trackeradapter

        binding.add.setOnClickListener {
            startActivity(Intent(requireContext(),AddTracker::class.java))
        }

        binding.filter.setOnClickListener {
            showDatePickerDialog()
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding.root
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = "$year-${month + 1}-$dayOfMonth"
                val filteredList = trackeradapter.getFilteredPests2(selectedDate)
                trackeradapter.updateData(filteredList)
            },
            2023, 0, 1 // Year, month (0-based), day
        )
        datePicker.show()
    }

    private fun filter(text: String) {
        val filteredList = trackeradapter.getFilteredPests(text)
        trackeradapter.updateData(filteredList)
    }

}