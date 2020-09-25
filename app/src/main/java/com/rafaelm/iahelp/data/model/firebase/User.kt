package com.rafaelm.iahelp.data.model.firebase


data class User(
    var name: String,
    var email: String,
    var number_phone: String,
    var password: String? = "",
)