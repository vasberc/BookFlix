package com.vasberc.domain.repo

import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.BookRemoteKey

interface BooksLocalRepo {
    suspend fun getBooksByPage(limit: Int, offset: Int): List<BookItem>
    suspend fun clearRemoteKeys()
    suspend fun clearAllEntities()

    suspend fun remoteKeysId(bookId: Int): BookRemoteKey?
}