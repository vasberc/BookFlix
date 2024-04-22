package com.vasberc.presentation.screens.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vasberc.domain.bookspaging.BooksPagingSource
import com.vasberc.domain.bookspaging.BooksRemoteMediator
import org.koin.android.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

@KoinViewModel
class BookListViewModel(
    mediator: BooksRemoteMediator
): ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    val pagerFlow by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                initialLoadSize = 20
            ),
            remoteMediator = mediator,
            pagingSourceFactory = {
                val pagingSource: BooksPagingSource by inject(
                    clazz = BooksPagingSource::class.java,
                    parameters = { parametersOf(mediator) }
                )
                pagingSource
            }
        ).flow.cachedIn(viewModelScope)
    }
}