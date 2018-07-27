package com.gelostech.automart.models

data class Message(
        var id: String? = null,
        var senderId: String? = null,
        var chatId: String? = null,
        var message: String? = null,
        var time: Long? = null,
        var isMe: Boolean? = null
) {
}