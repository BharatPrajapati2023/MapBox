package com.map.mapbox.MVVM_Login.Utils

import com.map.mapbox.MVVM_Login.data.RegisterBody
import com.map.mapbox.MVVM_Login.data.AuthResponse
import com.map.mapbox.MVVM_Login.data.LoginBody
import com.map.mapbox.MVVM_Login.data.UniqueEmailValidationResponse
import com.map.mapbox.MVVM_Login.data.ValidateEmaiBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiConsumer {
    @POST("user/validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmaiBody): Response<UniqueEmailValidationResponse>

    @POST("user/validate-unique-email")
    suspend fun registerUser(@Body body: RegisterBody): Response<AuthResponse>

    @POST("user/validate-unique-email")
    suspend fun loginUser(@Body body: LoginBody): Response<AuthResponse>
}