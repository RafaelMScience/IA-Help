package com.rafaelm.projecthermes.data.model.luis


data class ModelAzure(
    val prediction: Prediction = Prediction(),
    val query: String?
)