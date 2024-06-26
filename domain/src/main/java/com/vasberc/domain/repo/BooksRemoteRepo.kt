package com.vasberc.domain.repo

import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.RemoteData
import com.vasberc.domain.model.ResultState

interface BooksRemoteRepo {
    suspend fun getBooks(page: Int): ResultState<RemoteData>

    suspend fun getBookById(bookId: Int): ResultState<BookDetailed>
}