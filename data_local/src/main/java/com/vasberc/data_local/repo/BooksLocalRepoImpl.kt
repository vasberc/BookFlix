package com.vasberc.data_local.repo

import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.repo.BooksLocalRepo

class BooksLocalRepoImpl(
    private val booksDao: BookDao,
    private val remoteKeysDao: BookRemoteKeysDao
): BooksLocalRepo {
    override suspend fun getBooksByPage(limit: Int, offset: Int): List<BookItem> {
        return booksDao.getBooksByPage(limit, offset).map { it.asDomain() }
    }
}