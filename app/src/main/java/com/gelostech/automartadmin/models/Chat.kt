package com.gelostech.automartadmin.models

data class Chat(
        var id: String? = null,
        var uid1: String? = null,
        var uid2: String? = null,
        var senderId: String? = null,
        var message: String? = null,
        var time: Long? = null,
        var isMe: Boolean? = null
) {
}