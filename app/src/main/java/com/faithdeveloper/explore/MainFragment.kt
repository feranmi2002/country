package com.faithdeveloper.explore
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.databinding.IntroScreenBinding
import kotlinx.coroutines.launch

class MainFragment: Fragment(), FilterInterface {
    private  var _binding:IntroScreenBinding? = null
    private  val binding get() = _binding!!
    private lateinit var pagerAdapter:ExplorePager
    private lateinit var viewModel:ExploreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ExploreViewModel(ApiHelper(ServiceBuilder.apiService))
        setUpAdapter()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IntroScreenBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = pagerAdapter
        setUpLoadState()
        search()
        language()
        filter()
        observer()
        retry()
        darkOrLightMode()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun retry(){
        binding.feedBack.buttonRetry.setOnClickListener {
            pagerAdapter.retry()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun search() {
        binding.searchCountry?.setOnEditorActionListener { textView, actionId, keyEvent ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                        if (binding.searchCountry.text!!.isNotBlank()){
                            viewModel.nameQuery.value = binding.searchCountry.text.toString().trim()
                        }else{
                            Toast.makeText(requireContext(), "Enter a country", Toast.LENGTH_SHORT).show()
                        }
                    true
                }
                else -> false
            }
        }
    }

    private fun filter(){
        binding.filter.setOnClickListener {
            openBottomSheetDialog("filter")
        }
    }
    private fun language(){
        binding.languageChooser.setOnClickListener {
            openBottomSheetDialog("langauge")
        }
    }

    private fun openBottomSheetDialog(type:String){
        val bottomSheet = when(type){
            "filter" -> FilterBottomSheet.instance(this)
            else -> LanguageBottomSheet.instance(this)
        }
        bottomSheet.show(requireActivity().supportFragmentManager, FilterBottomSheet.TAG)
    }

    private fun darkOrLightMode(){
        binding.lightDarkMode.setOnClickListener {
            when(requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
                Configuration.UI_MODE_NIGHT_NO ->{
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
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment())
    }
    }
    private fun observer(){
        viewModel._result.observe(viewLifecycleOwner){
            pagerAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setUpLoadState() {
        lifecycleScope.launch {
            pagerAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && pagerAdapter.itemCount == 0
                // show empty list
                binding.feedBack.info.isVisible = isListEmpty

                //show loading spinner during initial load or refresh
                binding.feedBack.progressCircular.isVisible =
                    loadState.refresh is LoadState.Loading
                // show error info
                binding.feedBack.info.isVisible =
                    loadState.refresh is LoadState.Error && pagerAdapter.itemCount == 0
                binding.feedBack.buttonRetry.isVisible =
                    loadState.refresh is LoadState.Error && pagerAdapter.itemCount == 0
            }
        }
    }
}