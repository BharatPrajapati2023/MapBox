package com.map.mapbox.MVVM_Login.View_Model


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.mapbox.MVVM_Login.Repositery.AuthRepostory
import com.map.mapbox.MVVM_Login.Utils.AuthToken
import com.map.mapbox.MVVM_Login.Utils.RequestStatus
import com.map.mapbox.MVVM_Login.data.LoginBody
import com.map.mapbox.MVVM_Login.data.RegisterBody
import com.map.mapbox.MVVM_Login.data.User
import com.map.mapbox.MVVM_Login.data.ValidateEmaiBody
import kotlinx.coroutines.launch

class LoginActivityViewModel(val authRepostory: AuthRepostory, val application: Application) :
    ViewModel() {
    private var isLocading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var user: MutableLiveData<User> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLocading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getUser(): LiveData<User> = user
    fun loginUser(body: LoginBody) {
        viewModelScope.launch {
            authRepostory.loginUser(body).collect {
                when (it) {
                    is RequestStatus.Wating -> {
                        isLocading.value = true
                    }

                    is RequestStatus.Success -> {
                        isLocading.value = true
                        user.value = it.data.user
                        AuthToken.getInstance(application.baseContext).token = it.data.token
                    }

                    is RequestStatus.Error -> {
                        isLocading.value = false
                        errorMessage.value = it.message

                    }
                }
            }
        }
    }

}