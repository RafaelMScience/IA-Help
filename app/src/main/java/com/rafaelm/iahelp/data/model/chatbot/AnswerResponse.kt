package com.rafaelm.iahelp.data.model.chatbot


data class AnswerResponse(
    val answers: List<Answer> = listOf(),
    val activeLearningEnabled: Boolean
)
