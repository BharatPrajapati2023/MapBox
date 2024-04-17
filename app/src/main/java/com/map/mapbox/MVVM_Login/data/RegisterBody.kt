package com.map.mapbox.MVVM_Login.data

import java.io.Serializable

data class RegisterBody(val fullName: String, val email: String, val password: String) :
    Serializable
