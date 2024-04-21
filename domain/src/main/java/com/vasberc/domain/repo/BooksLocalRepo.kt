package com.vasberc.domain.repo

import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.BookRemoteKey

interface BooksLocalRepo {
    suspend fun getBooksByPage(limit: Int, offset: Int): List<BookItem>
    suspend fun clearRemoteKeys()
    suspend fun clearAllEntities()

    suspend fun getAllBooks(): List<BookItem>

    suspend fun getAllKeys(): List<BookRemoteKey>

    suspend fun getRemoteKeyById(bookId: Int): BookRemoteKey?
    suspend fun insertAllBookRemoteKeys(books: List<BookItem>, prevPage: Int?, nextPage: Int?, isRefresh: Boolean)

    suspend fun insertAllBooks(books: List<BookItem>, startingIndexOfPage: Int, isRefresh: Boolean)
}