package com.vasberc.data_remote.repo

import com.vasberc.data_remote.service.BookService
import com.vasberc.data_remote.utlis.mapToDomain
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksRemoteRepo
import org.koin.core.annotation.Single

@Single
class BooksRemoteRepoImpl(private val bookService: BookService): BooksRemoteRepo {
    override suspend fun getBooks(page: Int): ResultState<List<BookItem>> {
        return bookService.getBooks(page).mapToDomain()
    }

}