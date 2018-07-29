package com.gelostech.automart.models

data class Booking (
        var id: String? = null,
        var name: String? = null,
        var bookerId: String? = null,
        var bookerName: String? = null,
        var date: Long? = null,
        var sellerId: String? = null,
        var sellerName: String? = null,
        var dateBooked: String? = null,
        var timeBooked: String? = null,
        var image: String? = null,
        var holderImage: Int? = null
){
}