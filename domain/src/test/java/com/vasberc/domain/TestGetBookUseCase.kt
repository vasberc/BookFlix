package com.vasberc.domain

import com.vasberc.domain.fakeDb.FakeBookDao
import com.vasberc.domain.fakeDb.FakeBookRemoteKeysDao
import com.vasberc.domain.fakeDb.FakeDb
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksLocalRepo
import com.vasberc.domain.repo.BooksRemoteRepo
import com.vasberc.domain.repo.FakeLocalRepo
import com.vasberc.domain.repo.FakeRemoteRepo
import com.vasberc.domain.service.FakeService
import com.vasberc.domain.usecase.GetBookDetailedDataUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TestGetBookUseCase {
    private lateinit var repo: BooksRemoteRepo
    private lateinit var localRepo: BooksLocalRepo
    private lateinit var getBookDetailedDataUseCase: GetBookDetailedDataUseCase

    @Before
    fun setUp() {
        repo = FakeRemoteRepo(FakeService(null))
        val db = FakeDb()
        localRepo = FakeLocalRepo(FakeBookDao(db), FakeBookRemoteKeysDao(db))
        getBookDetailedDataUseCase = GetBookDetailedDataUseCase(repo, localRepo)
    }

    @Test
    fun testUseCase() = runTest {
        val flow = getBookDetailedDataUseCase(1)
        flow.collect {
           if(it is ResultState.Success) {
               val book = it.data
               assertEquals(book.id, 1)
           } else if(it is ResultState.Error) {
               assert(false)
           }
        }
    }

    @Test
    fun testUseCaseCache() = runTest {
        val flow = getBookDetailedDataUseCase(1)
        flow.collect {
           if(it is ResultState.Success) {
               val book = it.data
               val cached = localRepo.getDetailedBook(1)
               assertEquals(cached, book)
           } else if(it is ResultState.Error) {
               assert(false)
           }
        }
    }

}