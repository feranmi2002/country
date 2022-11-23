package com.faithdeveloper.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.R
import com.faithdeveloper.explore.adapters.FilterAdapter
import com.faithdeveloper.explore.databinding.FilterLayoutBinding
import com.faithdeveloper.explore.models.Filter
import com.faithdeveloper.explore.util.FilterInterface
import com.faithdeveloper.explore.util.Utils.CONTINENT
import com.faithdeveloper.explore.util.Utils.CONTINENT_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.TIME_ZONE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet() : BottomSheetDialogFragment() {
    private lateinit var filterType: String
    private lateinit var filterInterface: FilterInterface
    private var _binding: FilterLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null) {
            filterType = savedInstanceState.getString(FILTER_TYPE)!!
        }
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
        binding.filter.text = filterType
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
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
        adapter = FilterAdapter(
            when (filterType) {
                CONTINENT -> formatAdapterData(resources.getStringArray(R.array.continents))
                else -> formatAdapterData(resources.getStringArray(R.array.time_zones))
            }
        )
    }

    private fun formatAdapterData(array: Array<String>): MutableList<Filter> {
        val list = mutableListOf<Filter>()
        array.forEach {
            list.add(Filter(it, false))
        }
        return list
    }

    private fun showResults() {
        binding.showResult.setOnClickListener {
            if (!adapter.checkIfAnyCheckboxHasBeenChecked()) Toast.makeText(
                requireContext(), getString(
                    R.string.choose_filters
                ), Toast.LENGTH_SHORT
            ).show()
            else {
                val filter = adapter.getFilter()
                when (filterType) {
                    CONTINENT -> filterInterface.filter(
                        filter,
                        CONTINENT_QUERY_TYPE
                    )
                    TIME_ZONE -> filterInterface.filter(filter, TIME_ZONE)
                }
                dismiss()
            }
        }
    }

    private fun close() {
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(FILTER_TYPE, filterType)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val FILTER_TYPE = "filter_type"
        const val FILTER_INTERFACE = "filer_interface"
        fun instance(filterInterface: FilterInterface, filterType: String): FilterBottomSheet {
            return FilterBottomSheet().apply {
                this.filterInterface = filterInterface
                this.filterType = filterType
            }
        }

        const val TAG = "FilterBottomSheet"
    }
}