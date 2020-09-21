package com.rafaelm.projecthermes.data.api

import com.rafaelm.projecthermes.data.savetemp.Constants.Companion.keyPostChatApi
import com.rafaelm.projecthermes.data.model.chatbot.AnswerResponse
import com.rafaelm.projecthermes.data.model.chatbot.ChatRequest
import com.rafaelm.projecthermes.data.model.luis.ModelAzure
import retrofit2.Call
import retrofit2.http.*

interface NetworkServices {

    @GET("predict")
    fun postSendText(
        @QueryMap queryMap: Map<String,String>

    ): Call<ModelAzure>

    @POST("qnamaker/knowledgebases/${keyPostChatApi}/generateAnswer")
    fun postChat(@Header("Authorization")token: String, @Body question: ChatRequest): Call<AnswerResponse>

}

