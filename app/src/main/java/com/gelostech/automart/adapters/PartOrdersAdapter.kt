package com.gelostech.automart.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.databinding.ItemOrderBinding
import com.gelostech.automart.models.PartOrder
import com.gelostech.automart.utils.TimeFormatter
import com.gelostech.automart.utils.inflate
import com.gelostech.automart.utils.setDrawable
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.ionicons_typeface_library.Ionicons

class PartOrdersAdapter(private val context: Context) : RecyclerView.Adapter<PartOrdersAdapter.PartOrderHolder>() {
    private val parts = mutableListOf<PartOrder>()

    fun addPartOrder(part: PartOrder) {
        parts.add(part)
        notifyItemInserted(parts.size - 1)
    }

    fun addOrders(parts: MutableList<PartOrder>) {
        this.parts.addAll(parts)
        notifyDataSetChanged()
    }

    fun clearOrders() {
        parts.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartOrderHolder {
        return PartOrderHolder(parent.inflate(R.layout.item_order), context)
    }

    override fun getItemCount(): Int = parts.size

    override fun onBindViewHolder(holder: PartOrderHolder, position: Int) {
        holder.bind(parts[position])
    }

    class PartOrderHolder(private val binding: ItemOrderBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.user.setDrawable(setDrawable(context, Ionicons.Icon.ion_person, R.color.secondaryText, 14))
            binding.desc.setDrawable(setDrawable(context, FontAwesome.Icon.faw_shopping_basket, R.color.secondaryText, 14))
        }

        fun bind(part: PartOrder) {
            binding.data = part
            binding.isMine = (part.sellerId == FirebaseAuth.getInstance().currentUser?.uid)
        }

    }

}