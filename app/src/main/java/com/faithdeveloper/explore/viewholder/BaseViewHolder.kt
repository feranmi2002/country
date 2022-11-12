package com.faithdeveloper.explore.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
    open fun bind(item: Any) {
    }
}