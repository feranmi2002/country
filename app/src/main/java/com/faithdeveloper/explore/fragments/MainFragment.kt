package com.faithdeveloper.explore.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.R
import com.faithdeveloper.explore.databinding.IntroScreenBinding
import com.faithdeveloper.explore.paging.ExplorePager
import com.faithdeveloper.explore.retrofit.ApiHelper
import com.faithdeveloper.explore.retrofit.ServiceBuilder
import com.faithdeveloper.explore.util.FilterInterface
import com.faithdeveloper.explore.util.Utils
import com.faithdeveloper.explore.util.Utils.ALL_COUNTRIES_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.CAPITAL_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.CONTINENT_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.COUNTRY_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.CURRENCY_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.DEMONYM_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.LANGUAGE_QUERY_TYPE
import com.faithdeveloper.explore.viewmodels.ExploreViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.intro_screen.*
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : Fragment(), FilterInterface {
    private var _binding: IntroScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: ExplorePager
    private var pagerAdapterExternallyMadeEmpty = false
    private val viewModel: ExploreViewModel by viewModels {
        ExploreViewModel.factory(
            ApiHelper(
                ServiceBuilder.apiService
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setUpAdapter()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecycler()
        setUpLoadState()
        search()
        loadChips()
        language()
        observer()
        retry()
        darkOrLightMode()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadChips() {
        val chipData = resources.getTextArray(R.array.chips)
        binding.chipGroup.apply {
            setOnCheckedStateChangeListener { group, checkedIds ->
                if(checkedIds.size > 0) {
                    val chip: Chip? = group.findViewById(checkedIds[0])
                    viewModel.queryTypeCache = chip?.text as String
                    //               binding.searchCountry.setSimpleItems(autoCompleteResources[chip.text as String]!!)
                    binding.searchCountry.requestFocus()
                    Utils.showKeyboard(binding.root, requireContext())
                }

            }
            chipData.forEachIndexed { index, charSequence ->
                val chip =
                    layoutInflater.inflate(R.layout.chip_single, binding.chipGroup, false) as Chip
                chip.text = charSequence
                chip.isChecked = charSequence == viewModel.queryTypeCache
                addView(chip)
                chip.setOnClickListener {
                    if (binding.searchCountry.text.isNotBlank() && this.checkedChipIds.size > 0) {
                        request()
                    }
                }
            }
        }
    }

    private fun setUpRecycler() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = pagerAdapter
    }

    private fun retry() {
        binding.feedBack.buttonRetry.setOnClickListener {
            pagerAdapter.retry()
        }
    }

    private fun search() {
        binding.searchCountry.setOnEditorActionListener { textView, actionId, keyEvent ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (binding.searchCountry.text!!.isNotBlank()) {
                        if (binding.chipGroup.checkedChipIds.size == 0) {
                            binding.chipGroup.check(binding.chipGroup[0].id)
                        }
                        request()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_search_keyword),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun request() {
        Utils.hideKeyboard(binding.root, requireContext())
        pagerAdapterExternallyMadeEmpty = true
        pagerAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
        viewModel.query.value = binding.searchCountry.text.toString().trim()
    }

    private fun language() {
        binding.languageChooser.setOnClickListener {
            LanguageBottomSheet.instance(this)
                .show(requireActivity().supportFragmentManager, LanguageBottomSheet.TAG)
        }
    }

    private fun openOptionDialogs() {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        var dialog: AlertDialog? = null
        dialogBuilder.apply {
            setTitle(getString(R.string.filter_by))
            setMultiChoiceItems(
                resources.getStringArray(R.array.filter_headers),
                null
            ) { _, p1, _ ->
                if (p1 == 0) FilterBottomSheet.instance(this@MainFragment, CONTINENT_QUERY_TYPE)
                    .show(requireActivity().supportFragmentManager, FilterBottomSheet.TAG)
                else FilterBottomSheet.instance(this@MainFragment, "TIME_ZONE_QUERY_TYPE")
                    .show(requireActivity().supportFragmentManager, FilterBottomSheet.TAG)
                dialog?.cancel()
            }
            setCancelable(true)
            setNegativeButton(getString(R.string.cancel), object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
//                     do nothing
                }
            })
        }
        dialog = dialogBuilder.create()
        dialog.show()

    }

    private fun darkOrLightMode() {
        binding.lightDarkMode.setOnClickListener {
            when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.lightDarkMode.setImageResource(R.drawable.ic_moon)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.lightDarkMode.setImageResource(R.drawable.ic_sun_8726)
                }
            }
        }
    }

    override fun filter(filter: String, type: String) {
        binding.searchCountry.setText("")
        viewModel.queryType = type
        viewModel.query.value = filter
    }

    private fun setUpAdapter() {
        pagerAdapter = ExplorePager {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(
                    it
                )
            )
        }
    }

    private fun observer() {
        viewModel.result.observe(viewLifecycleOwner) {
            pagerAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setUpLoadState() {
        lifecycleScope.launch {
            pagerAdapter.loadStateFlow.collect { loadState ->
                // show empty list
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && pagerAdapter.itemCount < 1) {
                    recycler.isVisible = false

//                    empty list caused by new search
                    if (!pagerAdapterExternallyMadeEmpty) {
                        binding.feedBack.emptyResult.isVisible = true

                    }
                }
//                empty list caused by empty result
                else {
                    binding.feedBack.emptyResult.text =
                        Utils.emptyResultFeedback(requireContext(), viewModel.queryType).format(
                            Locale.getDefault(), viewModel.query.value)
                    binding.feedBack.emptyResult.isVisible = false
                    pagerAdapterExternallyMadeEmpty = false
                }
//                hide recycler on error or on loading
                binding.recycler.isVisible =
                    !(loadState.refresh is LoadState.Loading || loadState.refresh is LoadState.Error)

//                hide info on error or on loading
                binding.info.isVisible =
                    !(loadState.refresh is LoadState.Loading || loadState.refresh is LoadState.Error)

                //show loading spinner during initial load or refresh
                binding.feedBack.progressCircular.isVisible =
                    loadState.refresh is LoadState.Loading


                enableOrDisableTouchableViewsBasedOnLoadState(loadState.refresh is LoadState.Loading)

                // show error info
                binding.feedBack.error.isVisible =
                    loadState.refresh is LoadState.Error && pagerAdapter.itemCount == 0
                binding.feedBack.buttonRetry.isVisible =
                    loadState.refresh is LoadState.Error && pagerAdapter.itemCount == 0

//                show recycler on data loaded
                binding.info.isVisible =
                    loadState.refresh is LoadState.NotLoading && pagerAdapter.itemCount > 0
                binding.recycler.isVisible =
                    loadState.refresh is LoadState.NotLoading && pagerAdapter.itemCount > 0
                binding.info.text =
                    Utils.infoResource(requireContext(), viewModel.queryType).format(
                        Locale.getDefault(),
                        viewModel.query.value,
                        viewModel.responseSize
                    )
            }
        }
    }

    private fun enableOrDisableTouchableViewsBasedOnLoadState(isLoading: Boolean) {
        binding.lightDarkMode.isEnabled = !isLoading
        binding.chipGroup.isEnabled = !isLoading
        binding.languageChooser.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}