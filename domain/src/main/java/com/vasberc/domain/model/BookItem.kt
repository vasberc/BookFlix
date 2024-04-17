package com.vasberc.domain.model

data class BookItem(
    val id: Int,
    val title: String,
    val authors: List<String>,
    val image: String
)