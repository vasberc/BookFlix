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
        remoteKeysDao.clearRemoteKeys()
    }

    override suspend fun clearAllEntities() {
        booksDao.clearAllEntities()
    }

    override suspend fun getAllBooks(): List<BookItem> {
        return booksDao.getAllBooks().map { it.asDomain() }
    }

    override suspend fun getAllKeys(): List<BookRemoteKey> {
        return remoteKeysDao.getAllKeys().map { it.asDomain() }
    }

    override suspend fun getRemoteKeyById(bookId: Int): BookRemoteKey? {
        return remoteKeysDao.getRemoteKeyById(bookId)?.asDomain()
    }
}