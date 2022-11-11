package com.faithdeveloper.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faithdeveloper.explore.Utils.CHILD
import com.faithdeveloper.explore.Utils.PARENT
import com.faithdeveloper.explore.databinding.FilterItemBinding
import com.faithdeveloper.explore.databinding.LanguagesListItemBinding

class LanguageAdapter(
    private val languages: Array<String>,
   val  languageClick: (language:String) -> Unit,
    private val dataMapOfLanguage: MutableMap<String, Boolean>,
) : RecyclerView.Adapter<BaseViewHolder>() {

    inner class LanguageViewHolder(val binding: LanguagesListItemBinding) : BaseViewHolder(binding.root) {
        private val radioButton = binding.radioButton
        private var mItem: String? = null

        init {
            radioButton.setOnCheckedChangeListener { compoundButton, state ->
                languageClick.invoke(mItem!!)
            }
        }

        override fun bind(item: Any) {
            mItem = item as String
            binding.language.text = item
           radioButton.isChecked = dataMapOfLanguage[item]!!
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return LanguageViewHolder(
                LanguagesListItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount() = languages.size
}