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
        binding.country.text = data.name.official
        binding.regionHeader.text =
            Html.fromHtml("<b>Region: </b>${data.region}", Html.FROM_HTML_MODE_COMPACT)
        if (data.capital.isNotEmpty()) {
            binding.capitalHeader.text =
                Html.fromHtml("<b>Capital: </b>${data.capital.first()}", Html.FROM_HTML_MODE_COMPACT)
        }
        binding.timeZone.text = Html.fromHtml(
            "<b>Time Zone: </b> ${formatListOfStrings(data.timezones)}",
            Html.FROM_HTML_MODE_COMPACT
        )
        binding.officialLanguageHeader.text =
            Html.fromHtml("<b>Language:  </b>${data.languages.language}", Html.FROM_HTML_MODE_COMPACT)
        binding.area.text =
            Html.fromHtml("<b>Area: </b> ${data.area.toString()}", Html.FROM_HTML_MODE_COMPACT)
        binding.currency.text = Html.fromHtml(
            "<b>Currency: </b>${data.currencies.shortName.name} (${data.currencies.shortName.symbol})",
            Html.FROM_HTML_MODE_COMPACT
        )
        binding.mottoHeader.text =
            Html.fromHtml("<b>Motto: </b> ${data.motto}", Html.FROM_HTML_MODE_COMPACT)
        binding.independence.text = Html.fromHtml(
            "<b>Independent: </b>${data.independent}",
            Html.FROM_HTML_MODE_COMPACT
        )
    }

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