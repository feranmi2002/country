package com.faithdeveloper.explore

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
    open fun bind(item: FilterHeader) {
    }
}