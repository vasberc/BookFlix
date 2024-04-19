package com.vasberc.data_remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.vasberc.data_remote.model.BookResponse
import com.vasberc.data_remote.model.BooksResponse
import com.vasberc.data_remote.model.ErrorResponse
import retrofit2.Response
import java.io.IOException

class FakeService(private val error: Int?) : BookService {

    private fun getError(): Exception {
        return when(error) {
            NETWORK_ERROR -> IOException()
            SERVER_ERROR -> IllegalArgumentException()
            else -> Exception()
        }
    }

    private fun books(page: Int): List<BooksResponse.Result> {
        if(error != null) {
            throw getError()
        } else {
            val start = (page - 1) * 32
            val end = start + 32
            return (start until end).map { itemIndex ->
                BooksResponse.Result(
                    authors = (0..page).map { authorIndex ->
                        BooksResponse.Result.Author(
                            "Author$authorIndex"
                        )
                    },
                    formats = BooksResponse.Result.Formats("image$itemIndex"),
                    id = itemIndex,
                    title = "title$itemIndex"
                )
            }
        }
    }

    private fun book(id: Int): BookResponse {
        if(error != null) {
            throw getError()
        } else {
            //Calc the page to generate the same authors as the books end point
            val page = if(id == 0) 1 else 31 / id
            return BookResponse(
                authors = (0..page).map { authorIndex ->
                    BookResponse.Author(
                        name = "Author$authorIndex",
                        birthYear = authorIndex,
                        deathYear = authorIndex
                    )
                },
                formats = BookResponse.Formats("image$id"),
                id = id,
                subjects = (0..page).map { "subject$it" },
                title = "title$id"
            )
        }
    }

    override suspend fun getBooks(page: Int): NetworkResponse<BooksResponse, ErrorResponse> {
        return try {
            NetworkResponse.Success(
                body = BooksResponse(
                    results = books(page)
                ),
                response = Response.success("")
            )
        } catch (e: IOException) {
            NetworkResponse.NetworkError(e)
        } catch (e: IllegalArgumentException) {
            NetworkResponse.ServerError(ErrorResponse("not found"), null)
        } catch (e: Exception) {
            NetworkResponse.UnknownError(e, null)
        }
    }

    override suspend fun getBook(bookId: Int): NetworkResponse<BookResponse, ErrorResponse> {
        return try {
            NetworkResponse.Success(
                body = book(bookId),
                response = Response.success("")
            )
        } catch (e: IOException) {
            NetworkResponse.NetworkError(e)
        } catch (e: IllegalArgumentException) {
            NetworkResponse.ServerError(ErrorResponse("not found"), null)
        } catch (e: Exception) {
            NetworkResponse.UnknownError(e, null)
        }
    }

    companion object {
        const val NETWORK_ERROR = 0
        const val SERVER_ERROR = 1
        const val UNKNOWN_ERROR = 2
    }
}