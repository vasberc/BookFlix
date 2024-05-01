package com.vasberc.data_remote.model


import com.google.gson.annotations.SerializedName
import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.Domainable

data class BookResponse(
    @SerializedName("authors")
    val authors: List<Author?>?,
    @SerializedName("formats")
    val formats: Formats?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("subjects")
    val subjects: List<String?>?,
    @SerializedName("title")
    val title: String?
): Domainable<BookDetailed> {
    data class Author(
        @SerializedName("birth_year")
        val birthYear: Int?,
        @SerializedName("death_year")
        val deathYear: Int?,
        @SerializedName("name")
        val name: String?
    ): Domainable<BookDetailed.Author> {
        override fun toDomain(vararg args: Any): BookDetailed.Author {
            return BookDetailed.Author(
                name = name ?: "",
                birthYear = birthYear ?: -1,
                deathYear = deathYear ?: -1
            )
        }

    }

    data class Formats(
        @SerializedName("image/jpeg")
        val imagejpeg: String?
    ): Domainable<String> {
        override fun toDomain(vararg args: Any): String {
            return imagejpeg ?: ""
        }
    }

    override fun toDomain(vararg args: Any): BookDetailed {
        return BookDetailed(
            id = id ?: -1,
            authors = authors?.mapNotNull { it?.toDomain() } ?: listOf(),
            subject = subjects?.first() ?: "",
            image = formats?.toDomain() ?: "",
            title = title ?: ""
        )
    }
}