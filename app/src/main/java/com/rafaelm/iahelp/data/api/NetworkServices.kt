package com.rafaelm.iahelp.data.api

import com.rafaelm.iahelp.data.savetemp.Constants.Companion.keyPostChatApi
import com.rafaelm.iahelp.data.model.chatbot.AnswerResponse
import com.rafaelm.iahelp.data.model.chatbot.ChatRequest
import com.rafaelm.iahelp.data.model.luis.ModelAzure
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

