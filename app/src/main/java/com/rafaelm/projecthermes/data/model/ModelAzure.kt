package com.rafaelm.projecthermes.data.model


import com.google.gson.annotations.SerializedName

data class ModelAzure(
    val prediction: Prediction = Prediction(),
    val query: String?
)