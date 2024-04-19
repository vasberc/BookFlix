package com.vasberc.data_remote

import com.vasberc.data_remote.service.BookService
import com.vasberc.data_remote.service.FakeService
import com.vasberc.data_remote.utlis.mapToDomain
import com.vasberc.domain.model.ErrorModel
import com.vasberc.domain.model.ResultState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TestGetBooksByPageError {
    private lateinit var service: BookService

    @Test
    fun testIsError() = runTest {
        service = FakeService(0)
        val result = service.getBooks(1).mapToDomain()
        val isError = (result as? ResultState.Error) != null
        assertEquals(isError, true)
    }

    @Test
    fun testIsNetworkError() = runTest {
        service = FakeService(0)
        val result = service.getBooks(1).mapToDomain()
        val isNetworkError = (result as? ResultState.Error)?.error is ErrorModel.NetworkError
        assertEquals(isNetworkError, true)
    }
}