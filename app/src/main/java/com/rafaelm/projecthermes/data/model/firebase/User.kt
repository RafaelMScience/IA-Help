package com.rafaelm.projecthermes.data.model.firebase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name="email")
    var email: String,

    @ColumnInfo(name="number_phone")
    var number_phone: String,

    @ColumnInfo(name="login")
    var login: String,

    @ColumnInfo(name="password")
    var password: String,
)