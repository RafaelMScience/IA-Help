package com.rafaelm.projecthermes.data.model.chatbot


data class AnswerResponse(
    val answers: List<Answer> = listOf(),
    val activeLearningEnabled: Boolean
)
