package com.faithdeveloper.explore

import androidx.recyclerview.widget.DiffUtil

object ITEM_COMPARATOR : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.name.official == newItem.name.official
    }
}