package com.faithdeveloper.country.util

import androidx.recyclerview.widget.DiffUtil
import com.faithdeveloper.country.models.Country

object ITEM_COMPARATOR : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.name.official == newItem.name.official
    }
}