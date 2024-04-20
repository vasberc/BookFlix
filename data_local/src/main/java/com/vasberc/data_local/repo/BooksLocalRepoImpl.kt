package com.vasberc.data_local.repo

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.BookRemoteKey
import com.vasberc.domain.repo.BooksLocalRepo
import org.koin.core.annotation.Single

@Single
class BooksLocalRepoImpl(
    private val booksDao: BookDao,
    private val remoteKeysDao: BookRemoteKeysDao
): BooksLocalRepo {
    override suspend fun getBooksByPage(limit: Int, offset: Int): List<BookItem> {
        return booksDao.getBooksByPage(limit, offset).map { it.asDomain() }
    }

    override suspend fun clearRemoteKeys() {
        TODO("Not yet implemented")
    }

    override suspend fun clearAllEntities() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBooks(): List<BookItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllKeys(): List<BookRemoteKey> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteKeyById(bookId: Int): BookRemoteKey? {
        TODO("Not yet implemented")
    }
}