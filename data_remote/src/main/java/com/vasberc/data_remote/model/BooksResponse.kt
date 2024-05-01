package com.vasberc.data_remote.model


import com.google.gson.annotations.SerializedName
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.Domainable
import com.vasberc.domain.model.RemoteData

data class BooksResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("results")
    val results: List<Result?>?
): Domainable<RemoteData> {
    data class Result(
        @SerializedName("authors")
        val authors: List<Author?>?,
        @SerializedName("formats")
        val formats: Formats?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("title")
        val title: String?
    ): Domainable<BookItem> {
        data class Author(
            @SerializedName("name")
            val name: String?
        )

        data class Formats(
            @SerializedName("image/jpeg")
            val imagejpeg: String?
        )

        override fun toDomain(vararg args: Any): BookItem {
            return BookItem(
                id ?: -1,
                title ?: "",
                authors?.mapNotNull { it?.name } ?: listOf(),
                formats?.imagejpeg ?: ""
            )
        }
    }

    override fun toDomain(vararg args: Any): RemoteData {
        return RemoteData(
            totalItems = count ?: 0,
            currentPageItems = results?.mapNotNull { it?.toDomain() } ?: listOf()
        )
    }
}