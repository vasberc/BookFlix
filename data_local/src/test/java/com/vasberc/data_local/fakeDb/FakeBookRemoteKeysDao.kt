package com.vasberc.data_local.fakeDb

import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.data_local.entities.BookRemoteKeysEntity

class FakeBookRemoteKeysDao(private val db: FakeDb): BookRemoteKeysDao {
    override suspend fun clearRemoteKeys() {
        db.deleteAllRemoteKeys()
    }

    override suspend fun getRemoteKeyById(bookId: Int): BookRemoteKeysEntity? {
        return db.getRemoteKeys().find { it.bookId == bookId }
    }

    override suspend fun getAllKeys(): List<BookRemoteKeysEntity> {
        return db.getRemoteKeys()
    }

}