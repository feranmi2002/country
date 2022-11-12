package com.faithdeveloper.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.adapters.FilterAdapter
import com.faithdeveloper.explore.util.FilterInterface
import com.faithdeveloper.explore.R
import com.faithdeveloper.explore.util.Utils.CONTINENT
import com.faithdeveloper.explore.util.Utils.PARENT
import com.faithdeveloper.explore.util.Utils.REGION_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.TIME_ZONE
import com.faithdeveloper.explore.databinding.FilterLayoutBinding
import com.faithdeveloper.explore.models.FilterChild
import com.faithdeveloper.explore.models.FilterHeader
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet() : BottomSheetDialogFragment() {
    private lateinit var filterInterface: FilterInterface
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
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        reset()
        showResults()
        close()
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
        binding.showResult.setOnClickListener {
            if (adapter.checkAnyCheckboxHasBeenChecked() ==0) Toast.makeText(requireContext(), getString(
                R.string.choose_filters
            ), Toast.LENGTH_SHORT).show()
            else {
                val continents = adapter.getChosenContinents()
                filterInterface.filter(continents.first(), REGION_QUERY_TYPE)
                dismiss()
            }
        }
    }

    private fun close(){
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    companion object{
        fun instance(filterInterface: FilterInterface): FilterBottomSheet {
            return FilterBottomSheet().apply {
                this.filterInterface = filterInterface
            }
        }
        const val TAG = "FilterBottomSheet"
    }
}