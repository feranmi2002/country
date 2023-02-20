package com.faithdeveloper.country.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.faithdeveloper.country.databinding.DetailsListBinding

class CountryPropertiesAdapter(val data: List<String>, private val titles: Array<String>) :
    Adapter<CountryPropertiesAdapter.CountryPropertiesViewHolder>() {


    inner class CountryPropertiesViewHolder(private val binding: DetailsListBinding) :
        ViewHolder(binding.root) {


        fun bind(title: String, text: String) {
            binding.title.text = title
            binding.value.text = text
        }

    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        position: Int
    ): CountryPropertiesViewHolder {
        val binding =
            DetailsListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return CountryPropertiesViewHolder((binding))
    }

    override fun onBindViewHolder(holder: CountryPropertiesViewHolder, position: Int) {
        holder.bind(titles[position], data[position])
    }

    override fun getItemCount() = data.size - 3
}