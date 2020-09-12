package com.rafaelm.projecthermes.data.model

import com.google.gson.annotations.SerializedName


data class ModelAzure(
    val entities: List<Entity>? = listOf(),
    val query: String?,
    val topScoringIntent: TopScoringIntent = TopScoringIntent()
) {
    override fun toString(): String {
        return "{" +
                "query" + query +
                "topScoringIntent" + topScoringIntent +
                "entities" + entities
    }
}