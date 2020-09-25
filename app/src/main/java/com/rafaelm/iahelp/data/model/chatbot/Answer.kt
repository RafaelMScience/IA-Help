package com.rafaelm.iahelp.data.model.chatbot


data class Answer(
    val questions: List<String> = listOf(),
    val answer: String = "",
    val score: Double = 0.0,
    val id: Int?,
//    val metadata: List<Any>?
)