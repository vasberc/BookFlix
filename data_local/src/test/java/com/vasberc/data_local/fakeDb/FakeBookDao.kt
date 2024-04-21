package com.vasberc.data_local.fakeDb

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.entities.AuthorAndBookDetailedEntity
import com.vasberc.data_local.entities.AuthorDetailedEntity
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookDetailedWithRelations
import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.DetailedBookEntity

class FakeBookDao(private val fakeDb: FakeDb): BookDao {
    override suspend fun getBooksByPage(limit: Int, offset: Int): List<BookAndAuthorsEntity> {
        return fakeDb.getBooksAndAuthor().subList(offset, offset + limit)
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

    override suspend fun getDetailedBook(bookId: Int): BookDetailedWithRelations? {
        return null
    }

    override suspend fun getDetailedBooks(): List<BookDetailedWithRelations> {
        return listOf()
    }

    override suspend fun deleteDetailedBook(bookId: Int) {

    }

    override suspend fun deleteDetailedAuthorAndBook(bookId: Int) {

    }

    override suspend fun deleteDetailedAuthor(name: String) {

    }

    override suspend fun insertDetailedBook(book: DetailedBookEntity) {

    }

    override suspend fun insertAuthorDetailed(author: AuthorDetailedEntity) {

    }

    override suspend fun insertBookAndAuthorDetailed(authorAndBook: AuthorAndBookDetailedEntity) {

    }

    override suspend fun insertAllBookAndAuthors(
        bookAndAuthorsEntities: List<BookAndAuthorsEntity>,
        isRefresh: Boolean
    ) {
        if(isRefresh) {
            fakeDb.deleteAllBooks()
        }
        val authorEntities = mutableListOf<AuthorEntity>()
        insertAllBooks(bookAndAuthorsEntities.map {
            authorEntities.addAll(it.authorEntities)
            it.bookItemEntity
        })
        insertAllAuthors(authorEntities)
    }

}