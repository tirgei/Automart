package com.gelostech.automartadmin.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.models.ChatItem
import com.gelostech.automartadmin.utils.TimeFormatter
import com.gelostech.automartadmin.utils.inflate
import com.gelostech.automartadmin.utils.loadUrl
import kotlinx.android.synthetic.main.item_chat_list.view.*

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ChatListHolder>() {
    private var chats = mutableListOf<ChatItem>()

    fun addChat(chat: ChatItem) {
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        return ChatListHolder(parent.inflate(R.layout.item_chat_list))
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        holder.bindViews(chats[position])
    }

    class ChatListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatIcon = itemView.avatar
        private val chatUser = itemView.username
        private val chatTime = itemView.time
        private val chatMessage = itemView.message

        fun bindViews(chat: ChatItem) {
            with(chat) {
                chatIcon.loadUrl(avatar!!)
                chatUser.text = username
                chatTime.text = TimeFormatter().getChatTimeStamp(time!!)
                chatMessage.text = message
            }
        }

    }

}