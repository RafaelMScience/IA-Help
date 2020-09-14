package com.rafaelm.projecthermes.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitChatbot {

    companion object{
        private const val BASE_URL = "https://qnahefesto.azurewebsites.net/"

        private fun getRetrofitInstance(): Retrofit {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = (HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(RetrofitChatbot.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun apiConnection(): NetworkServices{
            return getRetrofitInstance().create(NetworkServices::class.java)
        }
    }
}