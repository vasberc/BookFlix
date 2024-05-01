package com.vasberc.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vasberc.domain.model.BookItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetBooksUseCase(
    private val pager: Pager<Int, BookItem>
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<BookItem>> {
        return pager
            .flow
            .cachedIn(scope)
    }
}