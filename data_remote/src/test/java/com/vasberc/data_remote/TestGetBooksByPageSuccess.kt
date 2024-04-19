package com.vasberc.data_remote

import com.vasberc.data_remote.service.BookService
import com.vasberc.data_remote.service.FakeService
import com.vasberc.data_remote.utlis.mapToDomain
import com.vasberc.domain.model.ResultState
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

    private lateinit var service: BookService

    @Before
    fun setUp() {
        service = FakeService(null)
    }

    @Test
    fun testGetBooksSuccess() = runTest {
        val result = service.getBooks(1).mapToDomain()
        val isSuccess = (result as? ResultState.Success) != null
        assertEquals(isSuccess, true)
    }

    @Test
    fun testGetBooksNotEmpty() = runTest {
        val result = service.getBooks(1).mapToDomain()
        val isNotEmpty = (result as? ResultState.Success)?.data?.isNotEmpty() == true
        assertEquals(isNotEmpty, true)
    }
}