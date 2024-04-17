package com.map.mapbox.MVVM_Login.data

import java.io.Serializable

data class AuthResponse(val user: User, val token: String) : Serializable
