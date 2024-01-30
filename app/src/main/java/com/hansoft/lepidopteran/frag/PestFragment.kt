package com.hansoft.lepidopteran.frag

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hansoft.lepidopteran.R
import com.hansoft.lepidopteran.database.chemicaldatabse.ChemicalViewModel
import com.hansoft.lepidopteran.database.pestsdatabase.PestViewModel
import com.hansoft.lepidopteran.databinding.FragmentPestBinding
import com.hansoft.lepidopteran.frag.fraghelpers.ChemicalAdapter
import com.hansoft.lepidopteran.frag.fraghelpers.PestAdapter

class PestFragment : Fragment() {
    private  lateinit var binding: FragmentPestBinding
    private lateinit var pestadapter: PestAdapter
    private lateinit var chemicaladapter: ChemicalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPestBinding.inflate(layoutInflater, container, false)

        binding.pest.setOnClickListener {
            binding.myRecyclerView.visibility = View.VISIBLE
            binding.chemicalRecyclerView.visibility = View.GONE
            binding.pest.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenColor))
            binding.chemical.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.chemical.setOnClickListener {
            binding.myRecyclerView.visibility = View.GONE
            binding.chemicalRecyclerView.visibility = View.VISIBLE
            binding.pest.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.chemical.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenColor))
        }

        binding.myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val pestViewModel = ViewModelProvider(this)[PestViewModel::class.java]
        pestadapter = PestAdapter(requireContext(),pestViewModel)
        pestViewModel.allPests.observe(viewLifecycleOwner) { pests ->
            pests?.let { pestadapter.setPests(it) }
        }
        binding.myRecyclerView.adapter = pestadapter

        binding.chemicalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val chemicalViewModel = ViewModelProvider(this)[ChemicalViewModel::class.java]
        chemicaladapter = ChemicalAdapter(requireContext(),chemicalViewModel)
        chemicalViewModel.allChemical.observe(viewLifecycleOwner) { pests ->
            pests?.let { chemicaladapter.setPests(it) }
        }
        binding.chemicalRecyclerView.adapter = chemicaladapter


        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding.root
    }

    private fun filter(text: String) {
        val filteredList = pestadapter.getFilteredPests(text)
        pestadapter.updateData(filteredList)
        val filteredList2 = chemicaladapter.getFilteredPests(text)
        chemicaladapter.updateData(filteredList2)
    }
}