package com.rafaelm.projecthermes.data.model.chatbot


import com.google.gson.annotations.SerializedName

data class ChatRequest(val question: String)

data class AnswerResponse(
    val answers: List<Answer> = listOf(),
    val activeLearningEnabled: Boolean
)

data class Answer(
    val questions: List<String> = listOf(),
    val answer: String = "",
    val score: Double = 0.0,
    val id: Int = 1,
    val metadata: List<Any>?
)