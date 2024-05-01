package com.vasberc.domain.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vasberc.domain.bookspaging.BooksPagingSource
import com.vasberc.domain.bookspaging.BooksRemoteMediator
import com.vasberc.domain.model.BookItem
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

@Module
@ComponentScan("com.vasberc.domain")
class DomainModule

@OptIn(ExperimentalPagingApi::class)
@Single
fun providePager(
    mediator: BooksRemoteMediator
): Pager<Int, BookItem> {
    return Pager(
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
    )
}
