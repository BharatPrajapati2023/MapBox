package com.map.mapbox.MVVM_Login.data

data class UniqueEmailValidationResponse(
    val isUnique: Boolean,
    val user: User
)
