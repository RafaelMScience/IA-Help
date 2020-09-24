package com.rafaelm.projecthermes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class EntityUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name="email")
    var email: String,

    @ColumnInfo(name="number_phone")
    var numberPhone: Int,

    @ColumnInfo(name="login")
    var login: String,

    @ColumnInfo(name="password")
    var password: String,
)