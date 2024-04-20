package com.vasberc.data_local

import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.fakeDb.FakeBookDao
import com.vasberc.data_local.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.data_local.fakeDb.FakeDb
import com.vasberc.data_local.repo.FakeLocalRepo
import com.vasberc.domain.repo.BooksLocalRepo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before

class TestGetBooksByPage {

    private lateinit var localRepo: BooksLocalRepo

    @Before
    fun setUp() {
        val db = FakeDb()
        val remoteKeysDao = FakeBookRemoteKeysDao()
        val bookDao = FakeBookDao(db)
        localRepo = FakeLocalRepo(bookDao, remoteKeysDao)
        repeat(50) {
            db.addBookIgnoreStrategy(
                BookItemEntity(
                    id = it, title = it.toString(), image = it.toString(), position = it

                )
            )
        }
    }

    @Test
    fun getFirstPageSuccess() = runTest {
        val books = localRepo.getBooksByPage(20, 0)
        assert(books.none { it.id >= 20 })
    }
    @Test
    fun getFirstPageError() = runTest {
        val books = localRepo.getBooksByPage(20, 0)
        assert(books.any { it.id >= 20 })
    }
    @Test
    fun getSecondPageSuccess() = runTest {
        val books = localRepo.getBooksByPage(20, 0)
        assert(books.none { it.id < 20 })
    }
    @Test
    fun getSecondPageError() = runTest {
        val books = localRepo.getBooksByPage(20, 0)
        assert(books.any { it.id < 20 })
    }
}