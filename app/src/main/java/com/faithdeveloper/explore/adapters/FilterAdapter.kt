package com.faithdeveloper.explore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faithdeveloper.explore.R
import com.faithdeveloper.explore.databinding.FilterHeaderBinding
import com.faithdeveloper.explore.databinding.FilterItemBinding
import com.faithdeveloper.explore.models.FilterChild
import com.faithdeveloper.explore.models.FilterHeader
import com.faithdeveloper.explore.util.Utils.CHILD
import com.faithdeveloper.explore.util.Utils.PARENT
import com.faithdeveloper.explore.viewholder.BaseViewHolder

class FilterAdapter(
    private var filters: MutableList<FilterHeader>,
    private val positionOfSecondHeader: Int
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var _filters: MutableList<FilterHeader> = filters

    inner class HeaderViewHolder(val binding: FilterHeaderBinding) : BaseViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.title.text = (item as FilterHeader).title
        }
    }

    inner class ChildViewHolder(private val binding: FilterItemBinding) :
        BaseViewHolder(binding.root) {
        private val checkBox = binding.checkbox
        private val text = binding.item
        private var mItem: FilterHeader? = null

        init {
            text.setOnClickListener {
                checkBox.performClick()
            }
            checkBox.setOnCheckedChangeListener { compoundButton, state ->
                filters[adapterPosition].child.data.first().checked = state
                if (state) clearPreviouslyCheckedBox(absoluteAdapterPosition)
            }
        }

        override fun bind(item: Any) {
            mItem = item as FilterHeader
            binding.item.text = item.child.data.first().title
            checkBox.isChecked = item.child.data.first().checked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            R.layout.filter_header -> {
                HeaderViewHolder(
                    FilterHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> ChildViewHolder(
                FilterItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (filters[position].type == PARENT) {
            holder as HeaderViewHolder
            holder.apply {
                binding.title.setOnClickListener {
                    expandOrCollapseHeader(filters[position], position)
                }
                binding.dropdownMenu.setOnClickListener {
                    expandOrCollapseHeader(filters[position], position)
                }
            }
        }
        holder.bind(filters[position])


    }

    override fun getItemCount() = filters.size
    override fun getItemId(position: Int) = position.toLong()

    private fun expandOrCollapseHeader(item: FilterHeader, adapterPosition: Int) {
        if (item.isExpanded) {
            collapseHeaderRow(item, adapterPosition)
        } else {
            expandParentRowPosition(item, adapterPosition)
        }
    }

    private fun expandParentRowPosition(item: FilterHeader, adapterPosition: Int) {
        item.isExpanded = true
        var nextPosition = adapterPosition
        if (item.type == PARENT) {
            item.child.data.forEach { subData ->
                val header = FilterHeader()
                header.type = CHILD
                val subChild = FilterChild()
                subChild.type = item.child.type
                subChild.data.add(subData)
                header.child = subChild
                filters.add(++nextPosition, header)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseHeaderRow(item: FilterHeader, adapterPosition: Int) {
        val currentChildren = item.child.data
        filters[adapterPosition].isExpanded = false
        var count = 0
        if (filters[adapterPosition].type == PARENT) {
            currentChildren.forEach { _ ->
                filters.removeAt(adapterPosition + 1)
                count += 1
            }
            notifyDataSetChanged()
        }
//        notifyItemRangeRemoved(adapterPosition + 1, adapterPosition + count)
    }

    fun reset() {
        filters = _filters
        notifyDataSetChanged()
    }

    fun checkIfAnyCheckboxHasBeenChecked(): Boolean {
        var countOfCheckedBoxes = 0
        var index = filters.size - 1
        do {
            if (filters[index].child.data.first().checked) countOfCheckedBoxes += 1
            else index -= 1
        }
        while (countOfCheckedBoxes == 0 && index >= 0)
        return countOfCheckedBoxes > 0
    }

    fun clearPreviouslyCheckedBox(position: Int) {
        var positionOfPreviouslyCheckedBox: Int? = null
        var index = 1
        do {
            val item = filters[index]
            if (item.type == PARENT && item.child.data.first().title == "GMT+1:00") {
                item.child.data.first().checked = false
            }
            if (item.child.data.first().checked && index != position) {
                positionOfPreviouslyCheckedBox = index
                item.child.data.first().checked = false
                notifyItemChanged(index)
            }
            index += 1
        }
        while (positionOfPreviouslyCheckedBox == null && index < filters.size)
    }

    fun getChosenChild(): FilterChild {
        var chosenChild: FilterChild? = null
        var index = 0
        do {
            if (filters[index].child.data.first().checked) chosenChild = filters[index].child
            index += 1
        }
        while (chosenChild == null)
        return chosenChild
    }

    override fun getItemViewType(position: Int): Int {
        return if (filters[position].type == PARENT) R.layout.filter_header
        else R.layout.filter_item
    }
}