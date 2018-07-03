package com.gelostech.automartadmin.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.models.Notification
import com.gelostech.automartadmin.utils.TimeFormatter
import com.gelostech.automartadmin.utils.inflate
import com.gelostech.automartadmin.utils.loadUrl
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

    class NotificationHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val notifAvatar = view.avatar
        private val notifAction = view.action
        private val notifText = view.summary
        private var notifTime = view.time

        fun bind(notification: Notification) {

            with(notification) {
                notifAvatar.loadUrl(avatar!!)
                notifAction.text = actionType
                notifText.text = summary
                notifTime.text = TimeFormatter().getChatTimeStamp(time!!)
            }

        }

    }

}