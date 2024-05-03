package com.vasberc.domain.repo

import com.vasberc.domain.model.BookDetailed

fun provideBook(bookId: Int, authorNames: List<String>): BookDetailed {
    return BookDetailed(
        id = bookId,
        title = bookId.toString(),
        authors = authorNames.map {
            BookDetailed.Author(
                name = it,
                birthYear = -1,
                deathYear = -1
            )
        },
        subject = "",
        image = ""
    )
}