package com.vasberc.domain.model

data class BookDetailed(
    val id: Int,
    val authors: List<Author>,
    val subject: String,
    val image: String
) {
    data class Author(
        val name: String,
        val birthYear: Int,
        val deathYear: Int
    )
}