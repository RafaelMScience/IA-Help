package com.rafaelm.projecthermes.data.model.chatbot


import com.google.gson.annotations.SerializedName

data class Context(
    val isContextOnly: Boolean = false,
    val prompts: List<Any> = listOf()
)