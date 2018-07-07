package com.gelostech.automart.models

data class Notification(
        var id: String? = null,
        var actionType: String? = null,
        var summary: String? = null,
        var avatar: Int? = null,
        var uid: String? = null,
        var time: Long? = null
) {
}