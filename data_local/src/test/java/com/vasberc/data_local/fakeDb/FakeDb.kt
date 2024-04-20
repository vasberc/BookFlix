package com.vasberc.data_local.fakeDb

import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.BookRemoteKeysEntity

class FakeDb {
    private val books = mutableListOf<BookItemEntity>()
    private val authors = mutableListOf<AuthorEntity>()
    private val remoteKeys = mutableListOf<BookRemoteKeysEntity>()

    fun editBook(book: BookItemEntity) {
        val index = books.indexOfFirst { it.id == book.id }
        if(index != -1) {
            books.removeAt(index)
            books.add(index, book)
        }
    }

    fun editRemoteKeys(remoteKeysEntity: BookRemoteKeysEntity) {
        val index = remoteKeys.indexOfFirst { it.bookId == remoteKeysEntity.bookId }
        if(index != -1) {
            remoteKeys.removeAt(index)
            remoteKeys.add(index, remoteKeysEntity)
        }
    }

    fun getBooks(): List<BookItemEntity> {
        return books.toList()
    }

    fun getAuthors(): List<AuthorEntity> {
        return authors.toList()
    }

    fun getRemoteKeys(): List<BookRemoteKeysEntity> {
        return remoteKeys.toList()
    }

    fun getBooksAndAuthor(): List<BookAndAuthorsEntity> {
        return books.asSequence().map { bookEntity ->
            BookAndAuthorsEntity(
                bookItemEntity = bookEntity,
                authorEntities = authors.asSequence().filter { it.bookId == bookEntity.id }.toList()
            )
        }.toList()
    }

    fun addBookIgnoreStrategy(book: BookItemEntity) {
        if(books.none { it.id == book.id }) {
            books.add(book)
            //Sqlite reorders the table by the primary key value
            books.sortBy { it.id }
        }
    }


    fun addBookReplaceStrategy(book: BookItemEntity) {
        val index = books.indexOfFirst { it.id == book.id || it.position == book.position }
        if(index != -1) {
            books.removeAt(index)
            books.add(index, book)
        } else {
            books.add(book)
        }
        //Sqlite reorders the table by the primary key value
        books.sortBy { it.id }
    }

    fun addAuthorIgnoreStrategy(authorEntity: AuthorEntity) {
        if(authors.none { it.bookId == authorEntity.bookId && it.name == authorEntity.name }) {
            authors.add(authorEntity)
        }
    }

    fun addRemoteKeyIgnoreStrategy(remoteKeysEntity: BookRemoteKeysEntity) {
        if(remoteKeys.none { it.bookId == remoteKeysEntity.bookId}) {
            remoteKeys.add(remoteKeysEntity)
            remoteKeys.sortBy { it.bookId }
        }
    }

    fun addRemoteKeyReplaceStrategy(remoteKeysEntity: BookRemoteKeysEntity) {
        val index = remoteKeys.indexOfFirst { it.bookId == remoteKeysEntity.bookId }
        if(index != -1) {
            remoteKeys.removeAt(index)
            remoteKeys.add(index, remoteKeysEntity)
        } else {
            remoteKeys.add(remoteKeysEntity)
        }
        remoteKeys.sortBy { it.bookId }
    }

    fun deleteBook(book: BookItemEntity) {
        deleteBook(book.id)
    }

    fun deleteBook(bookId: Int) {
        if(books.removeIf { it.id == bookId }) {
            //Cascade strategy
            deleteAuthor(bookId)
        }
    }

    fun deleteAuthor(bookId: Int) {
        authors.removeIf { it.bookId == bookId }
    }

    fun deleteAuthor(authorName: String) {
        authors.removeIf { it.name == authorName }
    }

    fun deleteAuthor(authorEntity: AuthorEntity) {
        authors.remove(authorEntity)
    }

    fun deleteRemoteKey(remoteKeysEntity: BookRemoteKeysEntity) {
        deleteRemoteKey(remoteKeysEntity.bookId)
    }

    fun deleteRemoteKey(bookId: Int) {
        remoteKeys.removeIf { it.bookId == bookId }
    }

    fun deleteAllBooks() {
        books.clear()
        //Cascade strategy
        deleteAllAuthors()
    }

    fun deleteAllAuthors() {
        authors.clear()
    }

    fun deleteAllRemoteKeys() {
        remoteKeys.clear()
    }

}