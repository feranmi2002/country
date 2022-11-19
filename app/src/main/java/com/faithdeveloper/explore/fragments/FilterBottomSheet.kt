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
import com.faithdeveloper.explore.models.FilterChild
import com.faithdeveloper.explore.models.FilterGrandChild
import com.faithdeveloper.explore.models.FilterHeader
import com.faithdeveloper.explore.util.FilterInterface
import com.faithdeveloper.explore.util.Utils.CONTINENT
import com.faithdeveloper.explore.util.Utils.PARENT
import com.faithdeveloper.explore.util.Utils.REGION_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.TIME_ZONE
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
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
        val headers = resources.getStringArray(R.array.filter_headers)
        val listOfHeaders = mutableListOf<FilterHeader>()

        val mutableListOfContinents = mutableListOf<FilterGrandChild>()
        resources.getStringArray(R.array.continents).forEach {
            mutableListOfContinents.add(FilterGrandChild(it, false))
        }
        val mutableListOfTimeZones = mutableListOf<FilterGrandChild>()
        resources.getStringArray(R.array.time_zones).toMutableList().forEach {
            mutableListOfTimeZones.add(FilterGrandChild(it, false))
        }

        val mapOfChildren = mapOf(
            CONTINENT to FilterChild(
                CONTINENT, mutableListOfContinents
            ),
            TIME_ZONE to FilterChild(
                TIME_ZONE, mutableListOfTimeZones
            )
        )
        headers.onEach {
            listOfHeaders.add(
                FilterHeader(it, PARENT, mapOfChildren[it]!!)
            )
        }
        adapter = FilterAdapter(listOfHeaders, mutableListOfContinents.size + 2)
    }

    private fun reset() {
        binding.reset.setOnClickListener {
            adapter.reset()
        }
    }

    private fun showResults() {
        binding.showResult.setOnClickListener {
            if (!adapter.checkIfAnyCheckboxHasBeenChecked()) Toast.makeText(
                requireContext(), getString(
                    R.string.choose_filters
                ), Toast.LENGTH_SHORT
            ).show()
            else {
                val filterChild = adapter.getChosenChild()
                when (filterChild.type) {
                    CONTINENT -> filterInterface.filter(
                        filterChild.data.first().title,
                        REGION_QUERY_TYPE
                    )
                    TIME_ZONE -> filterInterface.filter(filterChild.data.first().title, TIME_ZONE)
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

    companion object {
        fun instance(filterInterface: FilterInterface): FilterBottomSheet {
            return FilterBottomSheet().apply {
                this.filterInterface = filterInterface
            }
        }

        const val TAG = "FilterBottomSheet"
    }
}