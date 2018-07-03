package com.gelostech.automartadmin.models

data class ChatItem(
        var id: String? = null,
        var avatar: Int? = null,
        var username: String? = null,
        var time: Long? = null,
        var message: String? = null
) {
}