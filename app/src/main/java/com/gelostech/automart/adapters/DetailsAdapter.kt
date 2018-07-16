package com.gelostech.automart.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.databinding.ItemDetailsBinding
import com.gelostech.automart.databinding.ItemFeaturesBinding
import com.gelostech.automart.utils.inflate
import com.gelostech.automart.utils.setDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons

class DetailsAdapter(val context: Context) : RecyclerView.Adapter<DetailsAdapter.DetailsHolder>() {
    private val details = mutableMapOf<String, String>()

    fun addDetails(details: MutableMap<String, String>) {
        this.details.putAll(details)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder {
        return DetailsHolder(parent.inflate(R.layout.item_details), context)
    }

    override fun getItemCount(): Int = details.keys.size

    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {
        holder.bind(details.keys.toList()[position], details[details.keys.toList()[position]])
    }

    class DetailsHolder(private val binding: ItemDetailsBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(key: String?, value: String?) {

            binding.key = key!!
            binding.detail = value!!
        }

    }

}