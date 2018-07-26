package com.gelostech.automart.models

import java.io.Serializable

data class Car(
        var id: String? = null,
        var sellerName: String? = null,
        var sellerId: String? = null,
        var time: Long? = null,
        var make: String? = null,
        var model: String? = null,
        var location: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var description: String? = null,
        var mileage: String? = null,
        var transmission: String? = null,
        var year: String? = null,
        var condition: String? = null,
        var price: String? = null,
        var sold: Boolean? = false,
        var image: String? = null,
        var images: MutableMap<String, String> = mutableMapOf(),
        var features: MutableMap<String, Boolean> = mutableMapOf(),
        var watchlist: MutableMap<String, Boolean> = mutableMapOf(),
        var details: MutableMap<String, String> = mutableMapOf(),
        var holderImage: Int? = null,
        var holderAvatar: Int? = null
) : Serializable {
}