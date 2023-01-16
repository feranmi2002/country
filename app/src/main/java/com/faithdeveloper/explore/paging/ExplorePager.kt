package com.faithdeveloper.explore.paging

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faithdeveloper.explore.util.ITEM_COMPARATOR
import com.faithdeveloper.explore.databinding.CountriesListItemBinding
import com.faithdeveloper.explore.models.Country
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

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
            if (item?.capital?.isNotEmpty() == true) {
                binding.capital.text = item?.capital?.first()
            }
            binding.flag.loadImage(item?.flags?.png)
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

    private fun ShapeableImageView.loadImage(
        resource: Any?
    ) {
        getGlideImage(resource).into(this)
    }

    private fun ShapeableImageView.getGlideImage(
        resource: Any?
    ): RequestBuilder<Drawable> {
        return Glide.with(this.context)
            .load(resource)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    this@getGlideImage.post {
                        loadImage(resource)
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
    }
}