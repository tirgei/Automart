package com.gelostech.automart.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.callbacks.ChatListCallback
import com.gelostech.automart.databinding.ItemChatListBinding
import com.gelostech.automart.models.ChatItem
import com.gelostech.automart.utils.TimeFormatter
import com.gelostech.automart.utils.inflate
import com.gelostech.automart.utils.loadUrl
import kotlinx.android.synthetic.main.item_chat_list.view.*
import java.lang.ref.WeakReference

class ChatListAdapter(private val callback: ChatListCallback) : RecyclerView.Adapter<ChatListAdapter.ChatListHolder>() {
    private var chats = mutableListOf<ChatItem>()

    fun addChat(chat: ChatItem) {
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        return ChatListHolder(parent.inflate(R.layout.item_chat_list), callback)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        holder.bindViews(chats[position])
    }

    interface OnItemClickListener {
        fun onItemClickListener(chat: ChatItem)
    }

    class ChatListHolder(private val binding: ItemChatListBinding, callback: ChatListCallback) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.callback = callback
        }

        fun bindViews(chat: ChatItem) {
            binding.data = chat
            binding.time = TimeFormatter().getChatTimeStamp(chat.time!!)
        }

    }

}