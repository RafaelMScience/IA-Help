package com.rafaelm.iahelp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class EntityUser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 1,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name="email")
    var email: String,

    @ColumnInfo(name="number_phone")
    var numberPhone: String,
)