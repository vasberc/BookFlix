package com.vasberc.data_remote.repo

import com.vasberc.data_remote.service.FakeService
import com.vasberc.data_remote.utlis.mapToDomain
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksRemoteRepo

class FakeRepo(private val service: FakeService): BooksRemoteRepo {
    override suspend fun getBooks(page: Int): ResultState<List<BookItem>> {
        return service.getBooks(page).mapToDomain()
    }
}