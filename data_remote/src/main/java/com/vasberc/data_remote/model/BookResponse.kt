package com.vasberc.data_remote.model


import com.google.gson.annotations.SerializedName

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
) {
    data class Author(
        @SerializedName("birth_year")
        val birthYear: Int?,
        @SerializedName("death_year")
        val deathYear: Int?,
        @SerializedName("name")
        val name: String?
    )

    data class Formats(
        @SerializedName("image/jpeg")
        val imagejpeg: String?
    )
}