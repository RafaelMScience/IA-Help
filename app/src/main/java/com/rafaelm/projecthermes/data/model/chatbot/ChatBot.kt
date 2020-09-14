package com.rafaelm.projecthermes.data.model.chatbot


import com.google.gson.annotations.SerializedName

data class ChatBot(
    val activeLearningEnabled: Boolean = false,
    val answers: List<Answer>?
)