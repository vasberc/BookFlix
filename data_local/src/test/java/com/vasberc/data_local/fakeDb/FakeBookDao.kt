package com.vasberc.data_local.fakeDb

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.entities.BookAndAuthorsEntity

class FakeBookDao(private val fakeDb: FakeDb): BookDao {
    override suspend fun getBooksByPage(limit: Int, offset: Int): List<BookAndAuthorsEntity> {
        return fakeDb.getBooksAndAuthor().subList(offset * limit, offset + limit)
    }

    override suspend fun clearAllEntities() {
        fakeDb.deleteAllBooks()
    }

    override suspend fun getAllBooks(): List<BookAndAuthorsEntity> {
        return fakeDb.getBooksAndAuthor()
    }
}