package com.gelostech.automart.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
        var id: String? = null,
        var name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var token: String? = null,
        var dateCreated: String? = null,
        var dateModified: String? = null,
        var avatar: String? = null
): Serializable {
}