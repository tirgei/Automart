package com.gelostech.automart.models

import java.io.Serializable

data class Part(
        var id: String? = null,
        var name: String? = null,
        var number: String? = null,
        var category: String? = null,
        var description: String? = null,
        var sellerId: String? = null,
        var sellerName: String? = null,
        var location: String? = null,
        var price: String? = null,
        var time: Long? = null,
        var make: String? = null,
        var model: String? = null,
        var quantity: Int? = null,
        var image: String? = null,
        var images: MutableMap<String, String> = mutableMapOf(),
        var holderImage: Int? = null
): Serializable {
}