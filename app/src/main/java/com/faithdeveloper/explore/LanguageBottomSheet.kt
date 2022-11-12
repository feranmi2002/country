package com.faithdeveloper.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.databinding.LanguagesLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageBottomSheet() : BottomSheetDialogFragment() {
    private var _binding: LanguagesLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LanguageAdapter
    private lateinit var filterInterface:FilterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        createAdapter()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LanguagesLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        cancel()
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
        val dataMapOfLanguages = mutableMapOf<String, Boolean>()
        resources.getStringArray(R.array.languages).onEach {
            dataMapOfLanguages[it] = false
        }
        adapter = LanguageAdapter(
            resources.getStringArray(R.array.languages),
            languageClick = {
                TODO("Send to backend")
            }, dataMapOfLanguages
        )
    }

    private fun cancel() {
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    companion object{
        fun instance(filterInterface:FilterInterface):LanguageBottomSheet{
            return LanguageBottomSheet().apply {
                this.filterInterface = filterInterface
            }
        }
        const val TAG = "LanguageBottomSheet"
    }
}