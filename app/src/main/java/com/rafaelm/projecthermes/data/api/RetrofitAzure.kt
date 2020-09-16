package com.rafaelm.projecthermes.data.api

import com.rafaelm.projecthermes.data.dao.Constants.Companion.keyLuisApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAzure {
    companion object {
        private const val BASE_URL = "https://brazilsouth.api.cognitive.microsoft.com/luis/prediction/v3.0/apps/$keyLuisApi/slots/production/"

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