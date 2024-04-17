package com.map.mapbox.Ex2.Api

import com.map.mapbox.Ex2.Api.Constent.Companion.BASE_URL
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiClient {
    companion object{
        private val retrofit by lazy {
            val interceptor=HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client=OkHttpClient.Builder().addInterceptor(interceptor).build()

            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }

        val api by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }
}