package com.vasberc.data_local

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.fakeDb.FakeBookDao
import com.vasberc.data_local.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.data_local.fakeDb.FakeDb
import com.vasberc.data_local.repo.FakeLocalRepo
import com.vasberc.data_local.repo.provideBook
import com.vasberc.domain.repo.BooksLocalRepo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TestCacheBook {

    private lateinit var localRepo: BooksLocalRepo
    private lateinit var bookDao: BookDao

    @Before
    fun setUp() {
        val db = FakeDb()
        val remoteKeysDao = FakeBookRemoteKeysDao(db)
        bookDao = FakeBookDao(db)
        localRepo = FakeLocalRepo(bookDao, remoteKeysDao)
    }

    @Test
    fun cacheBook() = runTest {
        val book = provideBook(1, listOf("my author"))
        localRepo.cacheRemoteBook(book)
        val dbBook = localRepo.getDetailedBook(book.id)
        assertEquals(book, dbBook)
    }

    @Test
    fun cacheBookTwice() = runTest {
        val book1 = provideBook(1, listOf("my author"))
        val book2 = provideBook(1, listOf("my author"))
        localRepo.cacheRemoteBook(book2)
        localRepo.cacheRemoteBook(book1)
        val books = bookDao.getDetailedBooks()
        assertEquals(books.size, 1)
    }
}