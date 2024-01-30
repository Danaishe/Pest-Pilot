package com.hansoft.lepidopteran.frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.hansoft.lepidopteran.databinding.FragmentSettingBinding
import com.hansoft.lepidopteran.helpers.Util

class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding
    private val util = Util()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        binding.not.setText(util.getLocalData(requireContext(), "d")!!)
        binding.check.setOnClickListener {
            util.saveLocalData(requireContext(),"d",binding.not.text.toString())
            Toast.makeText(requireContext(),"Updated",Toast.LENGTH_SHORT).show()
        }

        updateUI()
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate(requireActivity())
        }

        return binding.root
    }

    private fun updateUI() {
        binding.themeSwitch.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

}