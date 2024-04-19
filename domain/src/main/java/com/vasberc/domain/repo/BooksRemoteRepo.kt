package com.vasberc.domain.repo

import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.ResultState

interface BooksRemoteRepo {
    suspend fun getBooks(page: Int): ResultState<List<BookItem>>
}