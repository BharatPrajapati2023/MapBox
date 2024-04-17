package com.map.mapbox.MVVM_Login.View_Model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.map.mapbox.MVVM_Login.Repositery.AuthRepostory
import java.security.InvalidParameterException

class RegisterActivityViewModelFactery(
    private val authRepostory: AuthRepostory,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)) {
            return RegisterActivityViewModel(authRepostory, application) as T
        }

        throw InvalidParameterException("Unable to Construct RegisterActivityViewModel")
    }
}