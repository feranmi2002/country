package com.faithdeveloper.explore.util

import androidx.recyclerview.widget.DiffUtil
import com.faithdeveloper.explore.models.Country

object ITEM_COMPARATOR : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.name.official == newItem.name.official
    }
}