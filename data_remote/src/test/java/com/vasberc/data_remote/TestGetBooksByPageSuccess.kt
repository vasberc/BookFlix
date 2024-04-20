package com.vasberc.data_remote

import com.vasberc.data_remote.repo.FakeRepo
import com.vasberc.data_remote.service.FakeService
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksRemoteRepo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
        val isNotEmpty = (result as? ResultState.Success)?.data?.currentPageItems?.isNotEmpty() == true
        assertEquals(isNotEmpty, true)
    }
}