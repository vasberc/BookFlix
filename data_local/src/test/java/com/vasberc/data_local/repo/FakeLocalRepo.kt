package com.vasberc.data_local.repo

import com.vasberc.data_local.fakeDb.FakeBookDao
import com.vasberc.data_local.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.BookRemoteKey
import com.vasberc.domain.repo.BooksLocalRepo

class FakeLocalRepo(private val fakeBookDao: FakeBookDao, private val fakeBookRemoteKeysDao: FakeBookRemoteKeysDao): BooksLocalRepo {
    override suspend fun getBooksByPage(limit: Int, offset: Int): List<BookItem> {
        return fakeBookDao.getBooksByPage(limit, offset).map { it.asDomain() }
    }

    override suspend fun clearRemoteKeys() {
        fakeBookRemoteKeysDao.clearRemoteKeys()
    }

    override suspend fun clearAllEntities() {
        fakeBookDao.clearAllEntities()
    }

    override suspend fun getAllBooks(): List<BookItem> {
        return fakeBookDao.getAllBooks().map { it.asDomain() }
    }

    override suspend fun getAllKeys(): List<BookRemoteKey> {
        return fakeBookRemoteKeysDao.getAllKeys().map { it.asDomain() }
    }

    override suspend fun getRemoteKeyById(bookId: Int): BookRemoteKey? {
        return fakeBookRemoteKeysDao.getRemoteKeyById(bookId)?.asDomain()
    }

    override suspend fun insertAllBookRemoteKeys(
        books: List<BookItem>,
        prevPage: Int?,
        nextPage: Int?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAllBooks(books: List<BookItem>, startingIndexOfPage: Int) {
        TODO("Not yet implemented")
    }
}