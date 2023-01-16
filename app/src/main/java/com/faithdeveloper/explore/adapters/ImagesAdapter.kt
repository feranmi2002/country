package com.faithdeveloper.explore.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faithdeveloper.explore.databinding.PicturesItemBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView

class ImagesAdapter(private val images: List<String>, val save: (bitmap: Bitmap) -> Unit) :
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: PicturesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var mUrl: String? = null

        init {
            binding.save.setOnClickListener {
                save.invoke(binding.picture.drawable.toBitmap())
            }

        }

        fun bind(url: String?) {
            mUrl = url
            binding.save.isVisible = false
            binding.progressCircular.isVisible = true
            binding.picture.loadImage(
                url,
                binding.progressCircular,
                binding.save
            )

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

    private fun ShapeableImageView.loadImage(
        resource: Any?,
        progressCircular: CircularProgressIndicator,
        save: MaterialTextView
    ) {
        getGlideImage(resource, progressCircular, save).into(this)
    }

    private fun ShapeableImageView.getGlideImage(
        resource: Any?,
        progressCircular: CircularProgressIndicator,

        save: MaterialTextView
    ): RequestBuilder<Drawable>{
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
                        loadImage(resource, progressCircular, save)
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
                    progressCircular.isVisible = false
                    save.isVisible = true
                    return false
                }

            })
    }
}