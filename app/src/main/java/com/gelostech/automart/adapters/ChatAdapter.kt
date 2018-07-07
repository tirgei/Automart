package com.gelostech.automart.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Chat
import com.gelostech.automart.utils.inflate
import kotlinx.android.synthetic.main.item_chat_me.view.*
import kotlinx.android.synthetic.main.item_chat.view.*

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
        return if (chats[position].isMe!!) {
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

    class ChatMeHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chatMessage = view.message

        fun bind(chat: Chat) {
            chatMessage.text = chat.message
        }

    }

    class ChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chatMessage = view.chat_message

        fun bind(chat: Chat) {
            chatMessage.text = chat.message
        }

    }
}