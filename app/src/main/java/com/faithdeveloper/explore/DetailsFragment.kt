package com.faithdeveloper.explore
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.faithdeveloper.explore.databinding.DetailsScreenBinding

class DetailsFragment: Fragment() {
    private var _binding:DetailsScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var data:Country
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    private fun presentData(){
        binding.country.text = data.name.official
        binding.regionHeader.text = data.region
        if (data.capital.isNotEmpty()) {
            binding.capitalHeader.text = "Capital: " + data.capital.first()
        }
        binding.timeZone.text = "Time Zone: " + formatListOfStrings(data.timezones)
        binding.officialLanguageHeader.text ="Language: " +  data.languages.language
        binding.area.text = "Area: " + data.area.toString()
        binding.currency.text = "Currency: "+ data.currencies.shortName.name + " (${data.currencies.shortName.symbol})"
        binding.mottoHeader.text = "Motto: " + data.motto
        binding.independence.text = "Independent: " +data.independent.toString()

    }

    private fun formatListOfStrings(timezones: List<String>):String {
        var string = ""
        var size = timezones.size
        timezones.forEach {
            string += it + if(size > 1) ", "
            else ""
            size -= 1
        }
return string
    }


    private fun back(){
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}