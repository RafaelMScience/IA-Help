package com.rafaelm.projecthermes.data.model


import com.google.gson.annotations.SerializedName

data class TopScoringIntent(
    val intent: String? = "",
    val score: Double? = 0.0
)