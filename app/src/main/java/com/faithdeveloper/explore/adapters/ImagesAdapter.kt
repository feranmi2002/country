package com.faithdeveloper.explore.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faithdeveloper.explore.databinding.PicturesItemBinding

class ImagesAdapter(private val images: List<String>, val save: (bitmap:Bitmap) -> Unit) :
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: PicturesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var mUrl: String? = null

        init {
            binding.retry.setOnClickListener {
                bind(mUrl)
            }

            binding.save.setOnClickListener {
                save.invoke(binding.picture.drawable.toBitmap())
            }
        }

        fun bind(url: String?) {
            mUrl = url
            binding.save.isVisible = false
            binding.retry.isVisible = false
            binding.progressCircular.isVisible = true
            Glide.with(itemView.context)
                .load(url)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressCircular.isVisible = false
                        binding.retry.isVisible = true
                        binding.save.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressCircular.isVisible = false
                        binding.retry.isVisible = false
                        binding.save.isVisible = true
                        return false
                    }

                })
                .into(binding.picture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            PicturesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() = images.size
}