package com.map.mapbox.MVVM_Login.Utils

import android.os.Message
import com.map.mapbox.Ex2.Api.Resource

sealed class RequestStatus<out T> {
    object Wating : RequestStatus<Nothing>()
    data class Success<out T>(val data: T) : RequestStatus<T>()
    data class Error(val message: HashMap<String, String>) : RequestStatus<Nothing>()

}