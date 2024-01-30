package com.hansoft.lepidopteran.views

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.hansoft.lepidopteran.database.chemicaldatabse.Chemical
import com.hansoft.lepidopteran.database.chemicaldatabse.ChemicalViewModel
import com.hansoft.lepidopteran.databinding.ActivityAddChemicalBinding

class AddChemical : AppCompatActivity() {
    private lateinit var binding: ActivityAddChemicalBinding
    var type = ""
    var icon = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChemicalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, com.hansoft.lepidopteran.R.color.greenColor)

        val items = arrayOf("organic", "biological")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                type = parentView?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                type = "organic"
            }
        }

        binding.red.setOnClickListener {
            binding.saf.text = "Red"
            icon = "red"
        }

        binding.yellow.setOnClickListener {
            binding.saf.text = "Yellow"
            icon = "yellow"
        }

        binding.blue.setOnClickListener {
            binding.saf.text = "Blue"
            icon = "blue"
        }

        binding.green.setOnClickListener {
            binding.saf.text = "Green"
            icon = "green"
        }

        binding.save.setOnClickListener {
            if(binding.name.text.isEmpty() || binding.target.text.isEmpty() ||
                icon == "" || binding.rate.text.isEmpty() || type == "" || binding.ScientificName.text.isEmpty()){
                Toast.makeText(this,"Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val ChemicalViewModel = ViewModelProvider(this)[ChemicalViewModel::class.java]
                val c = Chemical(
                    0, name = binding.name.text.toString(), rate = binding.rate.text.toString(),
                    target = binding.target.text.toString(), icon = icon,
                    type = type, scientificname = binding.ScientificName.text.toString()
                )
                ChemicalViewModel.insert(c)
                finish()
            }
        }

    }
}