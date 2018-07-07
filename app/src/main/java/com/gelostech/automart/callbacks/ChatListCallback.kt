package com.gelostech.automart.callbacks

import com.gelostech.automart.models.ChatItem

interface ChatListCallback {

    fun onClick(chat: ChatItem)

}