package com.gelostech.automart.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.callbacks.PartCallback
import com.gelostech.automart.databinding.ItemPartBinding
import com.gelostech.automart.models.Part
import com.gelostech.automart.utils.inflate
import kotlinx.android.synthetic.main.item_part.view.*

class PartsAdapter(private val callback: PartCallback) : RecyclerView.Adapter<PartsAdapter.PartHolder>() {
    private val parts = mutableListOf<Part>()

    fun addParts(part: Part) {
        parts.add(part)
        notifyItemInserted(parts.size - 1)
    }

    fun clearParts() {
        parts.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartHolder {
        val binding: ItemPartBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_part, parent, false)
        return PartHolder(binding, callback)
    }

    override fun getItemCount(): Int = parts.size

    override fun onBindViewHolder(holder: PartHolder, position: Int) {
        holder.bind(parts[position])
    }

    class PartHolder(private val binding: ItemPartBinding, callback: PartCallback) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.callback = callback
        }

        fun bind(part: Part) {
            binding.part = part
        }

    }

}