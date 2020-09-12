package com.rafaelm.projecthermes.data.model


import com.google.gson.annotations.SerializedName

data class Entity(
    val endIndex: Int?,
    val entity: String?,
//    val resolution: Any = Any(),
    val score: Double?,
    val startIndex: Int?,
    val type: String?
)