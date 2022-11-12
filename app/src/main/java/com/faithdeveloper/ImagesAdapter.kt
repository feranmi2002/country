package com.faithdeveloper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faithdeveloper.explore.databinding.PicturesItemBinding

class ImagesAdapter(private val images:List<String>):RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding:PicturesItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(url:String){
            Glide.with(itemView.context)
                .load(url)
                .into(binding.picture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(PicturesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() = images.size
}