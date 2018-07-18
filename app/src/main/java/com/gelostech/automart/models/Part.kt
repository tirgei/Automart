package com.gelostech.automart.models

import java.io.Serializable

data class Part(
        var id: String? = null,
        var name: String? = null,
        var sellerId: String? = null,
        var sellerName: String? = null,
        var price: Int? = null,
        var image: String? = null,
        var holderImage: Int? = null
): Serializable {
}