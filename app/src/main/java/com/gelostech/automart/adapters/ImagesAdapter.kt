package com.gelostech.automart.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.databinding.ItemImageBinding
import com.gelostech.automart.utils.inflate

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageHolder>() {
    private val images = mutableListOf<Uri>()

    fun addImages(images: MutableList<Uri>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    fun addImage(image: Uri) {
        this.images.add(image)
        notifyItemInserted(images.size -1 )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(parent.inflate(R.layout.item_image))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(images[position])
    }

    class ImageHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Uri) {
            binding.uri = image
        }

    }

}