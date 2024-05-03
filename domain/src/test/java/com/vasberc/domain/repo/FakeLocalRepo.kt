package com.vasberc.domain.repo

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.data_local.entities.asRemoteKeyEntity
import com.vasberc.data_local.entities.toEntity
import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.BookRemoteKey
import com.vasberc.domain.repo.BooksLocalRepo

class FakeLocalRepo(private val fakeBookDao: BookDao, private val fakeBookRemoteKeysDao: BookRemoteKeysDao): BooksLocalRepo {
    override suspend fun getBooksByPage(limit: Int, offset: Int): List<BookItem> {
        return fakeBookDao.getBooksByPage(limit, offset).map { it.toDomain() }
    }

    override suspend fun clearRemoteKeys() {
        fakeBookRemoteKeysDao.clearRemoteKeys()
    }

    override suspend fun clearAllEntities() {
        fakeBookDao.clearAllEntities()
    }

    override suspend fun getAllBooks(): List<BookItem> {
        return fakeBookDao.getAllBooks().map { it.toDomain() }
    }

    override suspend fun getAllKeys(): List<BookRemoteKey> {
        return fakeBookRemoteKeysDao.getAllKeys().map { it.toDomain() }
    }

    override suspend fun getRemoteKeyById(bookId: Int): BookRemoteKey? {
        return fakeBookRemoteKeysDao.getRemoteKeyById(bookId)?.toDomain()
    }

    override suspend fun insertAllBookRemoteKeys(
        books: List<BookItem>,
        prevPage: Int?,
        nextPage: Int?,
        isRefresh: Boolean
    ) {
        if(isRefresh) {
            fakeBookRemoteKeysDao.clearRemoteKeys()
        }
        fakeBookRemoteKeysDao.insertAll(books.map { it.asRemoteKeyEntity(prevPage, nextPage) })
    }

    override suspend fun insertAllBooks(books: List<BookItem>, startingIndexOfPage: Int, isRefresh: Boolean) {
        fakeBookDao.insertAllBookAndAuthors(books.mapIndexed { index, bookItem -> bookItem.toEntity(startingIndexOfPage + index) }, isRefresh)
    }

    override suspend fun getDetailedBook(bookId: Int): BookDetailed? {
        return fakeBookDao.getDetailedBook(bookId)?.toDomain()
    }

    override suspend fun cacheRemoteBook(data: BookDetailed) {
        fakeBookDao.insertDetailedBook(data.toEntity())
    }
}