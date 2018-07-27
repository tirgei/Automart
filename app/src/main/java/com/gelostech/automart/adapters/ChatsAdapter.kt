package com.gelostech.automart.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.callbacks.ChatListCallback
import com.gelostech.automart.databinding.ItemChatBinding
import com.gelostech.automart.models.Chat
import com.gelostech.automart.utils.TimeFormatter
import com.gelostech.automart.utils.inflate

class ChatsAdapter(private val callback: ChatListCallback) : RecyclerView.Adapter<ChatsAdapter.ChatListHolder>() {
    private var chats = mutableListOf<Chat>()

    fun addChat(chat: Chat) {
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    fun updateChat(updatedChat: Chat) {
        for ((index, chat) in chats.withIndex()) {
            if (updatedChat.id == chat.id) {
                chats[index] = updatedChat
                notifyItemChanged(index, updatedChat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        return ChatListHolder(parent.inflate(R.layout.item_chat), callback)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        holder.bindViews(chats[position])
    }

    interface OnItemClickListener {
        fun onItemClickListener(chat: Chat)
    }

    class ChatListHolder(private val binding: ItemChatBinding, callback: ChatListCallback) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.callback = callback
        }

        fun bindViews(chat: Chat) {
            binding.data = chat
            binding.time = TimeFormatter().getChatTimeStamp(chat.time!!)
        }

    }

}