package com.faithdeveloper.explore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faithdeveloper.explore.databinding.FilterItemBinding
import com.faithdeveloper.explore.models.Filter
import com.faithdeveloper.explore.viewholder.BaseViewHolder

class FilterAdapter(
    private var filters: MutableList<Filter>
) : RecyclerView.Adapter<BaseViewHolder>() {


    inner class FilterViewHolder(private val binding: FilterItemBinding) :
        BaseViewHolder(binding.root) {
        private val checkBox = binding.checkbox
        private val text = binding.item
        private var mItem: Filter? = null

        init {
            text.setOnClickListener {
                checkBox.performClick()
            }
            checkBox.setOnCheckedChangeListener { compoundButton, state ->
                filters[adapterPosition].checked = state
                if (state) clearPreviouslyCheckedBox(absoluteAdapterPosition)
            }
        }

        override fun bind(item: Any) {
            mItem = item as Filter
            binding.item.text = item.title
            checkBox.isChecked = item.checked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return FilterViewHolder(
            FilterItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    override fun getItemCount() = filters.size
    override fun getItemId(position: Int) = position.toLong()


    fun checkIfAnyCheckboxHasBeenChecked(): Boolean {
        var countOfCheckedBoxes = 0
        var index = filters.size - 1
        do {
            if (filters[index].checked) countOfCheckedBoxes += 1
            else index -= 1
        }
        while (countOfCheckedBoxes == 0 && index >= 0)
        return countOfCheckedBoxes > 0
    }

    fun clearPreviouslyCheckedBox(position: Int) {
        var positionOfPreviouslyCheckedBox: Int? = null
        var index = 0
        do {
            val item = filters[index]
            if (item.checked && index != position) {
                positionOfPreviouslyCheckedBox = index
                item.checked = false
                notifyItemChanged(index)
            }
            index += 1
        }
        while (positionOfPreviouslyCheckedBox == null && index < filters.size)
    }

    fun getFilter(): String {
        var filter = ""
        var index = filters.size - 1
        do {
            if (filters[index].checked) filter = filters[index].title
            index -=1
        }while(filter == "" && index >= 0)
        return filter
    }
}