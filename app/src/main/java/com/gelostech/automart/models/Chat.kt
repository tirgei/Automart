package com.gelostech.automart.models

data class Chat(
        var id: String? = null,
        var avatar: Int? = null,
        var username: String? = null,
        var time: Long? = null,
        var message: String? = null,
        var senderId: String? = null
) {
}