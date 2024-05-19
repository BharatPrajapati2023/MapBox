package com.map.mapbox.LiveInternetCheck



import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>
    enum class Status{
        Available,Unavailable,Losting,Lost
    }
}