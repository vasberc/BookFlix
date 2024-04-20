package com.vasberc.data_local

import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.BookRemoteKeysEntity
import com.vasberc.data_local.fakeDb.FakeBookDao
import com.vasberc.data_local.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.data_local.fakeDb.FakeDb
import com.vasberc.data_local.repo.FakeLocalRepo
import com.vasberc.domain.repo.BooksLocalRepo
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TestClearAll {
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
            db.addRemoteKeyIgnoreStrategy(
                BookRemoteKeysEntity(bookId = it, prevKey = null, nextKey = null)
            )
        }
    }

    @Test
    fun testClearAllEntitiesSuccess() = runTest {
        localRepo.clearAllEntities()
        val books = localRepo.getAllBooks()
        assert(books.isEmpty())
    }

    @Test
    fun testClearAllKeysSuccess() = runTest {
        localRepo.clearRemoteKeys()
        val books = localRepo.getAllKeys()
        assert(books.isEmpty())
    }
}