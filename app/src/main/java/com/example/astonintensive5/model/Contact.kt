package com.example.astonintensive5.model

data class Contact (
    val id: Int,
    var name: String,
    var surname: String,
    var number: String,
    val imageUrl: String = ""
)