package com.vasberc.data_local

import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.BookRemoteKeysEntity
import com.vasberc.data_local.fakeDb.FakeBookDao
import com.vasberc.data_local.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.data_local.fakeDb.FakeDb
import com.vasberc.data_local.repo.FakeLocalRepo
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.repo.BooksLocalRepo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TestInsert {
    private lateinit var localRepo: BooksLocalRepo
    private lateinit var bookItems1stPage: List<BookItem>
    private lateinit var bookItems2ndPage: List<BookItem>

    @Before
    fun setUp() {
        val db = FakeDb()
        val remoteKeysDao = FakeBookRemoteKeysDao(db)
        val bookDao = FakeBookDao(db)
        localRepo = FakeLocalRepo(bookDao, remoteKeysDao)
        bookItems1stPage = (0 until 20).map {
            BookItem(
                id = it,
                title = it.toString(),
                authors = listOf(it.toString()),
                image = it.toString()
            )
        }
        bookItems2ndPage = (20 until 30).map {
            BookItem(
                id = it,
                title = it.toString(),
                authors = listOf(it.toString()),
                image = it.toString()
            )
        }
    }

    @Test
    fun testInsert1stPageBooks() = runTest {
        localRepo.clearAllEntities()
        //This test helps also to check the starting index of page logic
        localRepo.insertAllBooks(bookItems1stPage, 1)
        localRepo.insertAllBooks(bookItems2ndPage, bookItems1stPage.size + 1)
        val books1stPage = localRepo.getBooksByPage( bookItems1stPage.size, 0)
        assertEquals(books1stPage, bookItems1stPage)
    }

    @Test
    fun testInsert2ndPageBooks() = runTest {
        localRepo.clearAllEntities()
        //This test helps also to check the starting index of page logic
        localRepo.insertAllBooks(bookItems1stPage, 1)
        localRepo.insertAllBooks(bookItems2ndPage, bookItems1stPage.size + 1)
        val books2ndPage = localRepo.getBooksByPage(bookItems2ndPage.size, bookItems1stPage.size)
        assertEquals(books2ndPage, bookItems2ndPage)
    }

}