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

    }

    override suspend fun clearAllEntities() {

    }

    override suspend fun remoteKeysId(bookId: Int): BookRemoteKey? {
        return null
    }
}