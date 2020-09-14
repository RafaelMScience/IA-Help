package com.rafaelm.projecthermes.data.api

import com.rafaelm.projecthermes.data.model.chatbot.Answer
import com.rafaelm.projecthermes.data.model.chatbot.AnswerResponse
import com.rafaelm.projecthermes.data.model.chatbot.ChatRequest
import com.rafaelm.projecthermes.data.model.luis.ModelAzure
import retrofit2.Call
import retrofit2.http.*

interface NetworkServices {

    @GET("predict")
    fun postSendText(
        @QueryMap queryMap: Map<String,String>

    ): retrofit2.Call<ModelAzure>

    @POST("qnamaker/knowledgebases/054a680d-188a-437b-b14a-84ba9ed0d517/generateAnswer")
    fun postChat(@Header("Authorization")token: String, @Body question: ChatRequest): Call<AnswerResponse>

}

