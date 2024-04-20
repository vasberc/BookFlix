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

class TestGetRemoteKeys {
    private lateinit var localRepo: BooksLocalRepo

    @Before
    fun setUp() {
        val db = FakeDb()
        val remoteKeysDao = FakeBookRemoteKeysDao(db)
        val bookDao = FakeBookDao(db)
        localRepo = FakeLocalRepo(bookDao, remoteKeysDao)
        repeat(50) {

            db.addRemoteKeyIgnoreStrategy(
                BookRemoteKeysEntity(it, null, null)
            )
        }
    }

    @Test
    fun getById() = runTest {
        val remoteKey = localRepo.getRemoteKeyById(1)
        assert(remoteKey != null)
    }

    @Test
    fun getByIdError() = runTest {
        val remoteKey = localRepo.getRemoteKeyById(50)
        assert(remoteKey == null)
    }

    @Test
    fun getAll() = runTest {
        val remoteKeys = localRepo.getAllKeys()
        assert(remoteKeys.isNotEmpty())
    }
}