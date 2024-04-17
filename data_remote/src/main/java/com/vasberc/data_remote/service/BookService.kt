package com.vasberc.data_remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.vasberc.data_remote.model.BookResponse
import com.vasberc.data_remote.model.BooksResponse
import com.vasberc.data_remote.model.ErrorResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {
    @GET("books/")
    suspend fun getBooks(
        @Query("page")
        page: Int
    ): NetworkResponse<BooksResponse, ErrorResponse>

    @GET("books/{book_id}")
    suspend fun getBook(
        @Path("book_id")
        bookId: Int
    ): NetworkResponse<BookResponse, ErrorResponse>
}