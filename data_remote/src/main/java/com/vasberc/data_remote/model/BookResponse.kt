package com.vasberc.data_remote.model


import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("authors")
    val authors: List<Author?>?,
    @SerializedName("bookshelves")
    val bookshelves: List<Any?>?,
    @SerializedName("copyright")
    val copyright: Boolean?,
    @SerializedName("download_count")
    val downloadCount: Int?,
    @SerializedName("formats")
    val formats: Formats?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("languages")
    val languages: List<String?>?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("subjects")
    val subjects: List<String?>?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("translators")
    val translators: List<Translator?>?
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
        @SerializedName("application/epub+zip")
        val applicationepubzip: String?,
        @SerializedName("application/octet-stream")
        val applicationoctetStream: String?,
        @SerializedName("application/rdf+xml")
        val applicationrdfxml: String?,
        @SerializedName("application/x-mobipocket-ebook")
        val applicationxMobipocketEbook: String?,
        @SerializedName("image/jpeg")
        val imagejpeg: String?,
        @SerializedName("text/html")
        val texthtml: String?,
        @SerializedName("text/html; charset=iso-8859-1")
        val texthtmlCharsetiso88591: String?,
        @SerializedName("text/plain; charset=us-ascii")
        val textplainCharsetusAscii: String?
    )

    data class Translator(
        @SerializedName("birth_year")
        val birthYear: Int?,
        @SerializedName("death_year")
        val deathYear: Int?,
        @SerializedName("name")
        val name: String?
    )
}