package com.faithdeveloper.explore.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faithdeveloper.explore.R
import com.faithdeveloper.explore.adapters.ImagesAdapter
import com.faithdeveloper.explore.databinding.DetailsScreenBinding
import com.faithdeveloper.explore.models.Country
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {
    private var _binding: DetailsScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: Country
    private lateinit var adapter: ImagesAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )
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
        adapter = ImagesAdapter(listOf(data.flags.png, data.coatOfArms.png)){
            downloadImage(it, binding.country.text.toString())
        }
    }

    private fun nextAndBack() {
        binding.backPicture.setOnClickListener {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 0) {
                binding.recycler.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1)
            } else {
                binding.recycler.smoothScrollToPosition(0)
            }
            binding.next.isVisible = true
            binding.backPicture.isVisible = false
        }
        binding.next.setOnClickListener {
            binding.recycler.smoothScrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1)
            binding.next.isVisible = false
            binding.backPicture.isVisible = true
        }
    }

    private fun downloadImage(bitmap: Bitmap?, name:String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val resolver = requireContext().applicationContext.contentResolver
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Find all image files on the primary external storage device.
                    val imageCollection =
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                    val newImageDetails = ContentValues().apply {
                        put(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            "$name -${System.currentTimeMillis()}.jpg"
                        )
                        put(MediaStore.MediaColumns.DATE_ADDED, dateFormatter.format(Date()))
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            "${Environment.DIRECTORY_PICTURES}/${resources.getString(R.string.app_name)}"
                        )
                    }
                    with(resolver) {
                        val imageUri = insert(imageCollection, newImageDetails)
                        resolver.openOutputStream(imageUri!!)?.use { stream ->
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                            stream.close()
                        }
                    }
                } else {
                    val directory =
                        requireContext().getExternalFilesDir("${Environment.DIRECTORY_PICTURES}/${resources.getString(R.string.app_name)}")
                    if (!directory?.exists()!!) directory.mkdir()
                    val newImageDetails = ContentValues().apply {
                        put(
                            MediaStore.Images.Media.DISPLAY_NAME,
                            "$name -${System.currentTimeMillis()}.jpg"
                        )
                        put(MediaStore.Images.Media.DATE_ADDED, dateFormatter.format(Date()))
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                    }
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newImageDetails)
                    val file = File(directory, "$name - ${System.currentTimeMillis()}.jpg")
                    val fileOutputStream = FileOutputStream(file)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.close()
                }
                Snackbar.make(requireView(), "Image Saved", Snackbar.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Snackbar.make(requireView(), "Failed to save image", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}