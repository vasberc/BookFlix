package com.vasberc.data_remote

import com.vasberc.data_remote.repo.FakeRepo
import com.vasberc.data_remote.service.BookService
import com.vasberc.data_remote.service.FakeService
import com.vasberc.data_remote.utlis.mapToDomain
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksRemoteRepo
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestGetBooksByPageSuccess {

    private lateinit var repo: BooksRemoteRepo

    @Before
    fun setUp() {
        repo = FakeRepo(FakeService(null))
    }

    @Test
    fun testGetBooksSuccess() = runTest {
        val result = repo.getBooks(1)
        val isSuccess = (result as? ResultState.Success) != null
        assertEquals(isSuccess, true)
    }

    @Test
    fun testGetBooksNotEmpty() = runTest {
        val result = repo.getBooks(1)
        val isNotEmpty = (result as? ResultState.Success)?.data?.isNotEmpty() == true
        assertEquals(isNotEmpty, true)
    }
}