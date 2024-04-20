package com.vasberc.data_local

import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.fakeDb.FakeBookDao
import com.vasberc.data_local.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.data_local.fakeDb.FakeDb
import com.vasberc.data_local.repo.FakeLocalRepo
import com.vasberc.domain.repo.BooksLocalRepo
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before

class TestGetBooks {

    private lateinit var localRepo: BooksLocalRepo

    @Before
    fun setUp() {
        val db = FakeDb()
        val remoteKeysDao = FakeBookRemoteKeysDao(db)
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
    fun getSecondPageSuccess() = runTest {
        //offset = page - 1 * limit
        val books = localRepo.getBooksByPage(20, 20)
        assert(books.none { it.id < 20 || it.id >= 40 })
    }

    @Test
    fun getAll() = runTest {
        val books = localRepo.getAllBooks()
        assert(books.isNotEmpty())
    }
}