package com.vasberc.domain.repo

import com.vasberc.data_remote.utlis.toDomain
import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.RemoteData
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksRemoteRepo
import com.vasberc.domain.service.FakeService

class FakeRemoteRepo(private val service: FakeService): BooksRemoteRepo {
    override suspend fun getBooks(page: Int): ResultState<RemoteData> {
        return service.getBooks(page).toDomain()
    }

    override suspend fun getBookById(bookId: Int): ResultState<BookDetailed> {
        return service.getBook(bookId).toDomain()
    }
}