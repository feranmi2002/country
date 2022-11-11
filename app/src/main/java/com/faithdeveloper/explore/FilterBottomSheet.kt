package com.faithdeveloper.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.faithdeveloper.explore.Utils.CONTINENT
import com.faithdeveloper.explore.Utils.PARENT
import com.faithdeveloper.explore.Utils.TIME_ZONE
import com.faithdeveloper.explore.databinding.FilterLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet() : BottomSheetDialogFragment() {
    private var _binding: FilterLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        createAdapter()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilterLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter
        reset()
        showResults()
        dismiss()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createAdapter() {
        val dataMapOfContinents = mutableMapOf<String, Boolean>()
        val dataMapOfTimeZones= mutableMapOf<String, Boolean>()
        val headers = resources.getStringArray(R.array.filter_headers)
        val listOfHeaders = mutableListOf<FilterHeader>()

        val mapOfChildren = mapOf(
            CONTINENT to FilterChild(
                CONTINENT,
                resources.getStringArray(R.array.continents).toMutableList()
            ),
            TIME_ZONE to FilterChild(
                TIME_ZONE,
                resources.getStringArray(R.array.time_zones).toMutableList()
            )
        )
        resources.getStringArray(R.array.continents).onEachIndexed { index, string ->
            dataMapOfContinents[string] = false
        }
        resources.getStringArray(R.array.time_zones).onEachIndexed { index, string ->
            dataMapOfTimeZones[string] = false
        }

        headers.onEach {
            listOfHeaders.add(
                FilterHeader(it, PARENT, mapOfChildren[it]!!)
            )
        }
        adapter = FilterAdapter(listOfHeaders, dataMapOfContinents, dataMapOfTimeZones)
    }

    private fun reset(){
        binding.reset.setOnClickListener {
            adapter.clearChecks()
        }
    }

    private fun showResults(){
        if (adapter.checkAnyCheckboxHasBeenChecked() ==0) Toast.makeText(requireContext(), getString(R.string.choose_filters), Toast.LENGTH_SHORT).show()
        else {
            val continents = adapter.getChosenContinents()
            val timeZones = adapter.getChosenTimeZones()
            TODO("Send to backend")
        }
    }

    private fun close(){
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}