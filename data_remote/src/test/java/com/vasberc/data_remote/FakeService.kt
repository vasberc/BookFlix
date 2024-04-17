package com.vasberc.data_remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.vasberc.data_remote.model.BookResponse
import com.vasberc.data_remote.model.BooksResponse
import com.vasberc.data_remote.model.ErrorResponse
import com.vasberc.data_remote.service.BookService
import retrofit2.Response
import java.io.IOException
import kotlin.random.Random

class FakeService(private val error: Boolean) : BookService {

    override suspend fun getBooks(page: Int): NetworkResponse<BooksResponse, ErrorResponse> {
        return if (!error) {
            NetworkResponse.Success(
                body = BooksResponse(
                    results = (0..Random.nextInt()).map {
                        BooksResponse.Result(
                            authors = (0..Random.nextInt(3)).map { authorIndex ->
                                BooksResponse.Result.Author(
                                    "Author$authorIndex"
                                )
                            },
                            formats = BooksResponse.Result.Formats("image$it"),
                            id = it,
                            title = "title$it"
                        )
                    }
                ),
                response = Response.success("")
            )
        } else {
            NetworkResponse.NetworkError(IOException())
        }
    }

    override suspend fun getBook(bookId: Int): NetworkResponse<BookResponse, ErrorResponse> {
        return if (!error) {
            NetworkResponse.Success(
                body = BookResponse(
                    authors = (0..Random.nextInt(3)).map { authorIndex ->
                        BookResponse.Author(
                            name = "Author$authorIndex",
                            birthYear = authorIndex,
                            deathYear = authorIndex
                        )
                    },
                    formats = BookResponse.Formats("image"),
                    id = 1,
                    subjects = (0..Random.nextInt(10)).map { "subject$it" },
                    title = "fake title"
                ),
                response = Response.success("")
            )
        } else {
            NetworkResponse.NetworkError(IOException())
        }
    }
}