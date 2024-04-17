package com.map.mapbox.MVVM_Login.Repositery

import com.map.mapbox.MVVM_Login.Utils.ApiConsumer
import com.map.mapbox.MVVM_Login.Utils.RequestStatus
import com.map.mapbox.MVVM_Login.Utils.SimplificeMessage
import com.map.mapbox.MVVM_Login.data.LoginBody
import com.map.mapbox.MVVM_Login.data.RegisterBody
import com.map.mapbox.MVVM_Login.data.ValidateEmaiBody
import kotlinx.coroutines.flow.flow


class AuthRepostory(private val consumere: ApiConsumer) {

    fun validateEmailAddress(body: ValidateEmaiBody) =
        flow {
            emit(RequestStatus.Wating)
            val response = consumere.validateEmailAddress(body)
            if (response.isSuccessful) {
                emit(RequestStatus.Success(response.body()!!))
            } else {
                emit(
                    RequestStatus.Error(
                        SimplificeMessage.get(
                            response.errorBody()!!.byteStream().reader().readText()
                        )
                    )
                )
            }
        }

    fun registerUser(body: RegisterBody) = flow {
        emit(RequestStatus.Wating)
        val response = consumere.registerUser(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplificeMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun loginUser(body: LoginBody) = flow {
        emit(RequestStatus.Wating)
        val response = consumere.loginUser(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(
                RequestStatus.Error(
                    SimplificeMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

}/*: Flow<RequestStatus<UniqueEmailValidationResponse>>*/