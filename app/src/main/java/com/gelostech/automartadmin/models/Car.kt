package com.gelostech.automartadmin.models

import java.io.Serializable

data class Car(
        var id: String? = null,
        var sellerName: String? = null,
        var sellerId: String? = null,
        var time: Long? = null,
        var make: String? = null,
        var model: String? = null,
        var location: String? = null,
        var lat: Long? = null,
        var lng: Long? = null,
        var mileage: Int? = null,
        var transmission: String? = null,
        var year: Int? = null,
        var cc: Int? = null,
        var fuel: String? = null,
        var driveType: String? = null,
        var condition: String? = null,
        var price: Int? = null,
        var holderImage: Int? = null,
        var holderAvatar: Int? = null
) : Serializable {
}