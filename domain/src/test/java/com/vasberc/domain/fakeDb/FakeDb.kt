package com.vasberc.domain.fakeDb

import com.vasberc.data_local.entities.AuthorAndBookDetailedEntity
import com.vasberc.data_local.entities.AuthorDetailedEntity
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookDetailedWithRelations
import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.BookRemoteKeysEntity
import com.vasberc.data_local.entities.DetailedBookEntity

class FakeDb {
    private val books = mutableListOf<BookItemEntity>()
    private val authors = mutableListOf<AuthorEntity>()
    private val remoteKeys = mutableListOf<BookRemoteKeysEntity>()

    private val booksDetailed = mutableListOf<DetailedBookEntity>()
    private val authorsDetailed = mutableListOf<AuthorDetailedEntity>()
    private val authorAndBookDetailed = mutableListOf<AuthorAndBookDetailedEntity>()

    fun insertBookDetailedIgnoreStrategy(book: DetailedBookEntity) {
        if(booksDetailed.none { it.id == book.id }) {
            booksDetailed.add(book.copy())
        }
    }

    fun insertAuthorDetailedIgnoreStrategy(author: AuthorDetailedEntity) {
        if(authorsDetailed.none { it.name == author.name }) {
            authorsDetailed.add(author.copy())
        }
    }

    fun insertBookAndAuthorDetailedIgnoreStrategy(authorAndBook: AuthorAndBookDetailedEntity) {
        if(authorAndBookDetailed.none { it.bookId == authorAndBook.bookId && it.name == authorAndBook.name }) {
            authorAndBookDetailed.add(authorAndBook.copy())
        }
    }

    fun getDetailedBooks(): List<BookDetailedWithRelations> {
        return booksDetailed.asSequence().mapNotNull {
            getDetailedBook(it.id)
        }.toList()
    }

    fun getDetailedBook(bookId: Int): BookDetailedWithRelations? {
        val book = booksDetailed.find { it.id == bookId } ?: return null
        val relation = authorAndBookDetailed.filter { it.bookId == book.id }
        val authors = authorsDetailed.filter { authorEntity ->
            authorEntity.name in relation.map { it.name }
        }

        return BookDetailedWithRelations(
            bookDetailed = book,
            authors = authors
        )
    }

    fun deleteDetailedBook(bookId: Int) {
        booksDetailed.removeIf { it.id == bookId }
        //Foreign key with cascade
        authorAndBookDetailed.removeIf { it.bookId == bookId }
    }

    fun deleteDetailedAuthorAndBook(bookId: Int) {
        authorAndBookDetailed.removeIf { it.bookId == bookId }
    }

    fun deleteDetailedAuthor(name: String) {
        //Foreign key with restrict
        if(authorAndBookDetailed.none { it.name == name }) {
            authorsDetailed.removeIf { it.name == name }
        }
    }

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
            books.add(book.copy())
            //Sqlite reorders the table by the primary key value
            books.sortBy { it.id }
        }
    }


    fun addBookReplaceStrategy(book: BookItemEntity) {
        val index = books.indexOfFirst { it.id == book.id || it.position == book.position }
        if(index != -1) {
            books.removeAt(index)
            books.add(index, book.copy())
        } else {
            books.add(book)
        }
        //Sqlite reorders the table by the primary key value
        books.sortBy { it.id }
    }

    fun addAuthorIgnoreStrategy(authorEntity: AuthorEntity) {
        if(authors.none { it.bookId == authorEntity.bookId && it.name == authorEntity.name }) {
            authors.add(authorEntity.copy())
        }
    }

    fun addRemoteKeyIgnoreStrategy(remoteKeysEntity: BookRemoteKeysEntity) {
        if(remoteKeys.none { it.bookId == remoteKeysEntity.bookId}) {
            remoteKeys.add(remoteKeysEntity.copy())
            remoteKeys.sortBy { it.bookId }
        }
    }

    fun addRemoteKeyReplaceStrategy(remoteKeysEntity: BookRemoteKeysEntity) {
        val index = remoteKeys.indexOfFirst { it.bookId == remoteKeysEntity.bookId }
        if(index != -1) {
            remoteKeys.removeAt(index)
            remoteKeys.add(index, remoteKeysEntity.copy())
        } else {
            remoteKeys.add(remoteKeysEntity.copy())
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