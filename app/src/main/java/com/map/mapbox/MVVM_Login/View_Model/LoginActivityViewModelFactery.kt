package com.map.mapbox.MVVM_Login.View_Model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.map.mapbox.MVVM_Login.Repositery.AuthRepostory
import java.security.InvalidParameterException

class LoginActivityViewModelFactery(
    private val authRepostory: AuthRepostory,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(authRepostory, application) as T
        }

        throw InvalidParameterException("Unable to Construct LoginActivityViewModel")
    }
}