package com.rafaelm.projecthermes.data.api

import com.rafaelm.projecthermes.data.model.ModelAzure
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NetworkServices {

    @GET("predict")
    fun postSendText(
        @QueryMap queryMap: Map<String,String>

    ): retrofit2.Call<ModelAzure>
}

