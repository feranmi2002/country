package com.faithdeveloper.country.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
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
import com.faithdeveloper.country.R
import com.faithdeveloper.country.databinding.IntroScreenBinding
import com.faithdeveloper.country.paging.ExplorePager
import com.faithdeveloper.country.retrofit.ApiHelper
import com.faithdeveloper.country.retrofit.ServiceBuilder
import com.faithdeveloper.country.util.Utils
import com.faithdeveloper.country.viewmodels.ExploreViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.intro_screen.*
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : Fragment(){
    private var _binding: IntroScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: ExplorePager
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
        loadAds()
        setUpRecycler()
        setUpLoadState()
        search()
        loadChips()
        observer()
        retry()
        darkOrLightMode()
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadAds(){
        binding.adView.loadAd(AdRequest.Builder().build())
    }

    private fun loadChips() {
        val chipData = resources.getTextArray(R.array.chips)
        binding.chipGroup.apply {
            setOnCheckedStateChangeListener { group, checkedIds ->
                if (checkedIds.size > 0) {
                    val chip: Chip? = group.findViewById(checkedIds[0])
                    viewModel.queryTypeCache = chip?.text as String
                    //               binding.searchCountry.setSimpleItems(autoCompleteResources[chip.text as String]!!)
                    binding.searchCountry.requestFocus()
                    Utils.showKeyboard(binding.root, requireContext())
                    binding.chipGroup.isSelectionRequired = true
                }

            }
            chipData.forEachIndexed { index, charSequence ->
                val chip =
                    layoutInflater.inflate(R.layout.chip_single, binding.chipGroup, false) as Chip
                chip.text = charSequence
                chip.isChecked = charSequence == viewModel.queryTypeCache
                addView(chip)
                chip.setOnClickListener {
                    binding.searchCountry.hint = when(chip.text){
                        "Country" -> resources.getString(R.string.country_hint)
                        "Continent" -> resources.getString(R.string.continent_hint)
                        "Sub-Continent" -> resources.getString(R.string.sub_continent_hint)
                        "Currency" -> resources.getString(R.string.currency_hint)
                        "Language" -> resources.getString(R.string.language_hint)
                        "Capital" -> resources.getString(R.string.capital_hint)
                        else -> resources.getString(R.string.demonymn_hint)
                    }
                    binding.chipGroup.isSelectionRequired = true
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
        viewModel.setPagerEmptyBecauseOfStartup()
        viewModel.setPagerExternallyMadeEmpty(true)
        pagerAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
        viewModel.query.value = binding.searchCountry.text.toString().trim()
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

//                set feedback image and text
                if (pagerAdapter.itemCount < 1) {

                    binding.feedBack.imageFeedback.setImageResource(
                        when (loadState.refresh) {
                            is LoadState.Error -> R.drawable.connection_error
                            else -> R.drawable.empty_result
                        }
                    )

                    binding.feedBack.textFeedback.text =
                        when (loadState.refresh) {
                            is LoadState.Error -> resources.getString(R.string.an_error_occurred)
                            is LoadState.NotLoading -> Utils.emptyResultFeedback(
                                requireContext(),
                                viewModel.queryType
                            ).format(
                                Locale.getDefault(), viewModel.query.value
                            )
                            else -> ""
                        }
                }


//                feedback visibility
                (
                        (loadState.source.refresh is LoadState.NotLoading &&
                                pagerAdapter.itemCount < 1
                                && !viewModel.pagerExternallyMadeEmpty
                                && !viewModel.pagerEmptyBecauseOfStartup
                                ) ||
                                (loadState.refresh is LoadState.Error && pagerAdapter.itemCount < 1)
                        ).apply {

                        binding.feedBack.textFeedback.isVisible = this
                        binding.feedBack.imageFeedback.isVisible = this
                    }

                binding.feedBack.buttonRetry.isVisible =
                    loadState.refresh is LoadState.Error && pagerAdapter.itemCount < 1


//               recycler and info visibility
                binding.info.isVisible =
                    loadState.refresh is LoadState.NotLoading && pagerAdapter.itemCount > 0
                binding.recycler.isVisible =
                    loadState.refresh is LoadState.NotLoading && pagerAdapter.itemCount > 0

//                set info based on query
                binding.info.text =
                    Utils.infoResource(requireContext(), viewModel.queryType).format(
                        Locale.ENGLISH,
                        viewModel.query.value,
                        viewModel.responseSize
                    )


                //show loading spinner and manage views during initial load or refresh
                (loadState.refresh is LoadState.Loading).apply {
                    binding.feedBack.progressCircular.isVisible = this
                    enableOrDisableTouchableViewsBasedOnLoadState(this)
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        binding.adView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    private fun enableOrDisableTouchableViewsBasedOnLoadState(isLoading: Boolean) {
        binding.lightDarkMode.isEnabled = !isLoading
        binding.chipGroup.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        binding.adView.destroy()
        _binding = null
        super.onDestroyView()
    }
}