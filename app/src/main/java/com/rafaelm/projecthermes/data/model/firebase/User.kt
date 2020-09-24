package com.rafaelm.projecthermes.data.model.firebase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class User(
    var name: String,
    var email: String,
    var number_phone: String,
    var password: String = "",
)