package com.map.mapbox.MVVM_Login.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    val fullName: String,
    val email: String
)