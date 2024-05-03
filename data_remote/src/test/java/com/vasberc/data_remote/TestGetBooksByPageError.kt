package com.vasberc.data_remote

import com.vasberc.data_remote.repo.FakeRepo
import com.vasberc.data_remote.service.FakeService
import com.vasberc.domain.model.Error
import com.vasberc.domain.model.ResultState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TestGetBooksByPageError {
    private lateinit var repo: FakeRepo

    @Test
    fun testIsServerError() = runTest {
        repo = FakeRepo(FakeService(FakeService.SERVER_ERROR))
        val result = repo.getBooks(1)
        val isServerError = (result as? ResultState.Error)?.error is Error.Server
        assertEquals(isServerError, true)
    }

    @Test
    fun testIsNetworkError() = runTest {
        repo = FakeRepo(FakeService(FakeService.NETWORK_ERROR))
        val result = repo.getBooks(1)
        val isNetworkError = (result as? ResultState.Error)?.error is Error.Network
        assertEquals(isNetworkError, true)
    }

    @Test
    fun testIsUnknownError() = runTest {
        repo = FakeRepo(FakeService(FakeService.UNKNOWN_ERROR))
        val result = repo.getBooks(1)
        val isUnknownError = (result as? ResultState.Error)?.error is Error.Unknown
        assertEquals(isUnknownError, true)
    }
}