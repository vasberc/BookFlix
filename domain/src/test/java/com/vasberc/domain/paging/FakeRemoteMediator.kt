package com.vasberc.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.vasberc.domain.bookspaging.BooksRemoteMediator
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.repo.BooksLocalRepo
import com.vasberc.domain.repo.BooksRemoteRepo

class FakeRemoteMediator(localRepo: BooksLocalRepo, remoteRepo: BooksRemoteRepo): BooksRemoteMediator(localRepo, remoteRepo) {

    var pageToLoad = 1
    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookItem>
    ): MediatorResult {

        return loadPage(pageToLoad, loadType)
    }
}