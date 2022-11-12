package com.faithdeveloper.explore.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faithdeveloper.explore.util.ITEM_COMPARATOR
import com.faithdeveloper.explore.databinding.CountriesListItemBinding
import com.faithdeveloper.explore.models.Country

class ExplorePager(private val onClick: (country: Country) -> Unit) :
    PagingDataAdapter<Country, ExplorePager.ExploreViewHolder>(ITEM_COMPARATOR) {

    inner class ExploreViewHolder(val binding: CountriesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mItem: Country? = null

        init {
            binding.item.setOnClickListener {
                onClick.invoke(mItem!!)
            }
        }

        fun bind(item: Country?) {
            mItem = item
            binding.country.text = item?.name?.official
            if (item?.capital?.isNotEmpty() == true){
                binding.capital.text = item?.capital?.first()
            }
            Glide.with(itemView.context)
                .load(item?.flags?.png)
                .into(binding.flag)
        }
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        return ExploreViewHolder(
            CountriesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}