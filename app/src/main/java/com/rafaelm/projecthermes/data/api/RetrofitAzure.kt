package com.rafaelm.projecthermes.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAzure {
    companion object {
        private val BASE_URL = "https://brazilsouth.api.cognitive.microsoft.com/luis/prediction/v3.0/apps/6d62b438-a94a-4d9a-ba53-d664b6336994/slots/production/"

        private fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun apiConnection(): NetworkServices{
            return getRetrofitInstance().create(NetworkServices::class.java)
        }
    }
}