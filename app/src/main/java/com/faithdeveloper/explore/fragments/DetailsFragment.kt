package com.faithdeveloper.explore.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.adapters.ImagesAdapter
import com.faithdeveloper.explore.databinding.DetailsScreenBinding
import com.faithdeveloper.explore.models.Country

class DetailsFragment : Fragment() {
    private var _binding: DetailsScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: Country
    private lateinit var adapter: ImagesAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        data = requireArguments().getParcelable("data")!!
        setUpAdapter()
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
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.layoutManager = linearLayoutManager
        binding.recycler.adapter = adapter
        presentData()
        back()
        nextAndBack()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun presentData() {
        binding.apply {
            country.text = data.name.official
            regionHeader.text = htmlFormat("Region", data.region)
            capitalHeader.text = htmlFormat("Capital", formatListOfStrings(data.capital))
            timeZone.text = htmlFormat("Time Zone(s)", formatListOfStrings(data.timezones))
            officialLanguageHeader.text = htmlFormat("Language", data.languages.language)
            area.text = htmlFormat("Area", data.area.toString())
            currency.text = htmlFormat("Currency", data.currencies.shortName.name)
            mottoHeader.text = htmlFormat("Motto", data.motto)
            independence.text = htmlFormat("Independent", data.independent.toString())
            unMember.text = htmlFormat("UN Member", data.unMember.toString())
            subRegion.text = htmlFormat("Sub Region", data.subregion)
            landlocked.text = htmlFormat("Landlocked", data.landlocked.toString())
            startOfWeek.text = htmlFormat("Start Of The Week", data.startOfWeek)
            latitude.text = htmlFormat("Latitude", data.latlng[0].toString())
            longitude.text = htmlFormat("Longitude", data.latlng[1].toString())
            googleMaps.text = htmlFormat("Google Map", data.maps.googleMaps)
            openStreetMap.text = htmlFormat("Open Street Map", data.maps.openStreetMaps)
            drivingSide.text = htmlFormat("Driving Side", data.car.side)
        }

    }

    private fun htmlFormat(header: String, item: String) =
        Html.fromHtml("<b>$header: </b> $item", Html.FROM_HTML_MODE_COMPACT)

    private fun formatListOfStrings(timezones: List<String>): String {
        var string = ""
        var size = timezones.size
        timezones.forEach {
            string += it + if (size > 1) ", "
            else ""
            size -= 1
        }
        return string
    }


    private fun back() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpAdapter() {
        adapter = ImagesAdapter(listOf(data.flags.png, data.coatOfArms.png))
    }

    private fun nextAndBack() {
        binding.backPicture.setOnClickListener {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                binding.recycler.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1)
            } else {
                binding.recycler.smoothScrollToPosition(0)
            }
        }
        binding.next.setOnClickListener {
            binding.recycler.smoothScrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}