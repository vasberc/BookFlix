package com.vasberc.data_remote.repo

import com.vasberc.data_remote.service.BookService
import com.vasberc.data_remote.utlis.mapToDomain
import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.RemoteData
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksRemoteRepo
import org.koin.core.annotation.Single

@Single
class BooksRemoteRepoImpl(private val bookService: BookService): BooksRemoteRepo {
    override suspend fun getBooks(page: Int): ResultState<RemoteData> {
        return bookService.getBooks(page).mapToDomain()
    }

    override suspend fun getBookById(bookId: Int): ResultState<BookDetailed> {
        return bookService.getBook(bookId).mapToDomain()
    }
}