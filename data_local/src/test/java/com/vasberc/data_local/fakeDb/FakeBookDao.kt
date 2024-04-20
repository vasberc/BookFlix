package com.vasberc.data_local.fakeDb

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookItemEntity

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

    override suspend fun insertAllBooks(bookEntities: List<BookItemEntity>) {
        bookEntities.forEach {
            fakeDb.addBookIgnoreStrategy(it)
        }
    }

    override suspend fun insertAllAuthors(authorEntities: List<AuthorEntity>) {
        authorEntities.forEach {
            fakeDb.addAuthorIgnoreStrategy(it)
        }
    }

}