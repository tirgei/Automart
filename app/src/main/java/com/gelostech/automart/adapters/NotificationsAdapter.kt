package com.gelostech.automart.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.databinding.ItemNotifBinding
import com.gelostech.automart.models.Notification
import com.gelostech.automart.utils.TimeFormatter
import com.gelostech.automart.utils.inflate
import com.gelostech.automart.utils.loadUrl
import kotlinx.android.synthetic.main.item_notif.view.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationHolder>() {
    private val notifs = mutableListOf<Notification>()

    fun addNotif(notification: Notification) {
        notifs.add(notification)
        notifyItemInserted(notifs.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(parent.inflate(R.layout.item_notif))
    }

    override fun getItemCount(): Int = notifs.size

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(notifs[position])
    }

    class NotificationHolder(private val binding: ItemNotifBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.notif = notification
            binding.time = TimeFormatter().getTimeStamp(notification.time!!)
        }

    }

}