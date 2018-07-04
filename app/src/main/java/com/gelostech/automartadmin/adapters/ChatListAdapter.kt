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
import java.lang.ref.WeakReference

class ChatListAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<ChatListAdapter.ChatListHolder>() {
    private var chats = mutableListOf<ChatItem>()

    fun addChat(chat: ChatItem) {
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        return ChatListHolder(parent.inflate(R.layout.item_chat_list), onItemClickListener)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        holder.bindViews(chats[position])
    }

    interface OnItemClickListener {
        fun onItemClickListener(chat: ChatItem)
    }

    class ChatListHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val root = itemView.root
        private val chatIcon = itemView.avatar
        private val chatUser = itemView.username
        private val chatTime = itemView.time
        private val chatMessage = itemView.message
        private val weakReference = WeakReference<OnItemClickListener>(onItemClickListener)

        private lateinit var chat: ChatItem

        init {
            root.setOnClickListener(this)
        }

        fun bindViews(chat: ChatItem) {
            this.chat = chat

            with(chat) {
                chatIcon.loadUrl(avatar!!)
                chatUser.text = username
                chatTime.text = TimeFormatter().getChatTimeStamp(time!!)
                chatMessage.text = message
            }
        }

        override fun onClick(v: View?) {
            when(v?.id) {
                root.id -> weakReference.get()!!.onItemClickListener(chat)
            }
        }
    }

}