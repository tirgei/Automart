package com.gelostech.automart.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.commoners.K
import com.gelostech.automart.databinding.ItemChatBinding
import com.gelostech.automart.databinding.ItemChatMeBinding
import com.gelostech.automart.models.Chat
import com.gelostech.automart.utils.inflate
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chats = mutableListOf<Chat>()

    fun addChat(chat: Chat) {
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    fun lastPosition(): Int = chats.size - 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == K.ME) {
            ChatMeHolder(parent.inflate(R.layout.item_chat_me))
        } else {
            ChatHolder(parent.inflate(R.layout.item_chat))
        }
    }

    override fun getItemCount(): Int = chats.size

    override fun getItemViewType(position: Int): Int {
        return if (chats[position].senderId == FirebaseAuth.getInstance().currentUser!!.uid) {
            K.ME
        } else {
            K.OTHER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatHolder) {
            holder.bind(chats[position])
        } else if (holder is ChatMeHolder) {
            holder.bind(chats[position])
        }

    }

    class ChatMeHolder(private val binding: ItemChatMeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.chat = chat
        }

    }

    class ChatHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.chat = chat
        }

    }
}