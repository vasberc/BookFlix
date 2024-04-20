package com.vasberc.domain.model

data class BookRemoteKey(
    val bookId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)