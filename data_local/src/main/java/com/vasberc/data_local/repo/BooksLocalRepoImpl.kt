package com.vasberc.data_local.repo

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.data_local.entities.toEntity
import com.vasberc.data_local.entities.asRemoteKeyEntity
import com.vasberc.domain.model.BookDetailed
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
        return booksDao.getBooksByPage(limit, offset).map { it.toDomain() }
    }

    override suspend fun clearRemoteKeys() {
        remoteKeysDao.clearRemoteKeys()
    }

    override suspend fun clearAllEntities() {
        booksDao.clearAllEntities()
    }

    override suspend fun getAllBooks(): List<BookItem> {
        return booksDao.getAllBooks().map { it.toDomain() }
    }

    override suspend fun getAllKeys(): List<BookRemoteKey> {
        return remoteKeysDao.getAllKeys().map { it.toDomain() }
    }

    override suspend fun getRemoteKeyById(bookId: Int): BookRemoteKey? {
        return remoteKeysDao.getRemoteKeyById(bookId)?.toDomain()
    }

    override suspend fun insertAllBookRemoteKeys(
        books: List<BookItem>,
        prevPage: Int?,
        nextPage: Int?,
        isRefresh: Boolean
    ) {
        val entities = books.map { it.asRemoteKeyEntity(prevPage, nextPage) }
        if(isRefresh) {
            remoteKeysDao.insertAllWithRefresh(entities)
        } else {
            remoteKeysDao.insertAll(entities)
        }
    }

    override suspend fun insertAllBooks(books: List<BookItem>, startingIndexOfPage: Int, isRefresh: Boolean) {
        booksDao.insertAllBookAndAuthors(books.mapIndexed { index, bookItem -> bookItem.toEntity(startingIndexOfPage + index) }, isRefresh)
    }

    override suspend fun getDetailedBook(bookId: Int): BookDetailed? {
        return booksDao.getDetailedBook(bookId)?.toDomain()
    }

    override suspend fun cacheRemoteBook(data: BookDetailed) {
        booksDao.insertDetailedBook(data.toEntity())
    }
}