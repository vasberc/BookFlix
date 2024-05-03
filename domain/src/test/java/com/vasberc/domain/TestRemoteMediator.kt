package com.vasberc.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import com.vasberc.domain.fakeDb.FakeBookDao
import com.vasberc.domain.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.domain.fakeDb.FakeDb
import com.vasberc.domain.repo.BooksLocalRepo
import com.vasberc.domain.repo.BooksRemoteRepo
import com.vasberc.domain.repo.FakeLocalRepo
import com.vasberc.domain.repo.FakeRemoteRepo
import com.vasberc.domain.service.FakeService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestRemoteMediator {
    private lateinit var repo: BooksRemoteRepo

    private lateinit var localRepo: BooksLocalRepo

    private lateinit var remoteMediator: FakeRemoteMediator

    @Before
    fun setUp() {
        repo = FakeRemoteRepo(FakeService(null))
        val db = FakeDb()
        localRepo = FakeLocalRepo(FakeBookDao(db), FakeBookRemoteKeysDao(db))
        remoteMediator = FakeRemoteMediator(localRepo, repo)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun testLoad1stPage() = runTest {
        remoteMediator.pageToLoad = 1
        remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                null,
                PagingConfig(20, 5, true, 20),
                20
            )
        )
        val allBooks = localRepo.getAllBooks()
        assertEquals(allBooks.size, 32)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun testLoadRefresh() = runTest {
        remoteMediator.pageToLoad = 1
        remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                null,
                PagingConfig(20, 5, true, 20),
                20
            )
        )
        remoteMediator.pageToLoad = 2
        remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                null,
                PagingConfig(20, 5, true, 20),
                20
            )
        )
        val allBooks = localRepo.getAllBooks()
        assertEquals(allBooks.size, 32)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun testLoad2Pages() = runTest {
        remoteMediator.pageToLoad = 1
        remoteMediator.load(
            LoadType.REFRESH,
            PagingState(
                pages = listOf(),
                null,
                PagingConfig(20, 5, true, 20),
                20
            )
        )
        remoteMediator.pageToLoad = 2
        remoteMediator.load(
            LoadType.APPEND,
            PagingState(
                pages = listOf(),
                null,
                PagingConfig(20, 5, true, 20),
                20
            )
        )
        val allBooks = localRepo.getAllBooks()
        assertEquals(allBooks.size, 64)
    }
}