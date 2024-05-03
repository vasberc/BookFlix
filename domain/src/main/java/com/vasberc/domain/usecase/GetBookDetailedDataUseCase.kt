package com.vasberc.domain.usecase

import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.Error
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksLocalRepo
import com.vasberc.domain.repo.BooksRemoteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
class GetBookDetailedDataUseCase(
    private val remoteRepo: BooksRemoteRepo,
    private val localRepo: BooksLocalRepo
) {

    operator fun invoke(bookId: Int): Flow<ResultState<BookDetailed>> {
        return flow {
            emit(ResultState.Loading)
            //First check if is cached to avoid some network requests
            val localBook: BookDetailed? = localRepo.getDetailedBook(bookId)
            if(localBook != null) {
                emit(ResultState.Success(localBook))
            } else {
                val remoteBookState = remoteRepo.getBookById(bookId)
                if(remoteBookState is ResultState.Success) {
                    localRepo.cacheRemoteBook(remoteBookState.data)
                    emit(remoteBookState)
                } else {
                    emit(ResultState.Error((remoteBookState as? ResultState.Error)?.error ?: Error.Unknown()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}