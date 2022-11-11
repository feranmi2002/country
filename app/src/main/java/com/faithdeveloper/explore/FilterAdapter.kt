package com.faithdeveloper.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faithdeveloper.explore.Utils.CHILD
import com.faithdeveloper.explore.Utils.CONTINENT
import com.faithdeveloper.explore.Utils.PARENT
import com.faithdeveloper.explore.databinding.FilterHeaderBinding
import com.faithdeveloper.explore.databinding.FilterItemBinding

class FilterAdapter(
    private val filters: MutableList<FilterHeader>,
    private val dataMapOfContinents: MutableMap<String, Boolean>,
    private val dataMapOfTimeZones: MutableMap<String, Boolean>
) : RecyclerView.Adapter<BaseViewHolder>() {

    inner class HeaderViewHolder(val binding: FilterHeaderBinding) : BaseViewHolder(binding.root) {
        override fun bind(item: Any) {
            binding.title.text = (item as FilterHeader).title
        }
    }

    inner class ChildViewHolder(val binding: FilterItemBinding) : BaseViewHolder(binding.root) {
        private val checkBox = binding.checkbox
        private var mItem: FilterHeader? = null

        init {
            checkBox.setOnCheckedChangeListener { compoundButton, state ->
                when (mItem?.child?.type) {
                    CONTINENT -> dataMapOfContinents[mItem?.child?.data!!.first()] = state
                    else -> dataMapOfTimeZones[mItem?.child?.data!!.first()] = state
                }
            }
        }

        override fun bind(item: Any) {
            mItem = item as FilterHeader
            binding.item.text = item.child.data.first()
            if (item.child.type == CONTINENT) checkBox.isChecked =
                dataMapOfContinents[item.child.data.first()]!!
            else checkBox.isChecked = dataMapOfTimeZones[item.child.data.first()]!!
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

    fun clearChecks() {
        dataMapOfTimeZones.onEachIndexed { index, entry ->
            dataMapOfTimeZones[entry.key] = false
        }
        dataMapOfContinents.onEach {
            dataMapOfContinents[it.key] = false
        }
        notifyDataSetChanged()
    }

    fun checkAnyCheckboxHasBeenChecked(): Int {
        var countOfCheckedBoxes = 0
        var index = dataMapOfTimeZones.size
        var listOfData = dataMapOfTimeZones.toList()
        do {
            if (listOfData[index].second) countOfCheckedBoxes += 1
            else index -= 1
        }
        while (countOfCheckedBoxes == 0 && index > 0)

        if (countOfCheckedBoxes == 0) {
            listOfData = dataMapOfContinents.toList()
            index = dataMapOfContinents.size
            do {
                if (listOfData[index].second) countOfCheckedBoxes += 1
                else index -= 1
            }
            while (countOfCheckedBoxes == 0 && index > 0)
        }
        return countOfCheckedBoxes
    }

    fun getChosenContinents(): List<String> {
        val continents = mutableListOf<String>()
        dataMapOfContinents.onEach {
            if (it.value) continents.add(it.key)
        }
        return continents
    }

    fun getChosenTimeZones(): List<String> {
        val timeZones = mutableListOf<String>()
        dataMapOfTimeZones.onEach {
            if (it.value) timeZones.add(it.key)
        }
        return timeZones
    }

    override fun getItemViewType(position: Int): Int {
        return if (filters[position].type == PARENT) R.layout.filter_header
        else R.layout.filter_item
    }
}