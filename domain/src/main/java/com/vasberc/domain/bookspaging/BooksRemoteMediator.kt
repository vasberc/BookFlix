package com.vasberc.domain.bookspaging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.BookRemoteKey
import com.vasberc.domain.model.ResultState
import com.vasberc.domain.repo.BooksLocalRepo
import com.vasberc.domain.repo.BooksRemoteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
abstract class BooksRemoteMediator(
    protected val localRepo: BooksLocalRepo,
    protected val remoteRepo: BooksRemoteRepo
): RemoteMediator<Int, BookItem>() {
    var remoteDataTotalItems: Int? = null
}

@Single
class BooksRemoteMediatorImpl(
    localRepo: BooksLocalRepo,
    remoteRepo: BooksRemoteRepo
): BooksRemoteMediator(localRepo, remoteRepo) {
    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookItem>
    ): MediatorResult {
        return withContext(Dispatchers.IO)  {
            Timber.d("BooksRemoteMediatorImpl new loadState=$loadType")
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    //Set to null, so the paging source will know that the state is loading
                    remoteDataTotalItems = null
                    //There refresh is our implementation can be triggered only in the top
                    //element when the user makes pull to refresh, so we have to clear the data
                    //because we will get from network all the data again
                    localRepo.clearRemoteKeys()
                    localRepo.clearAllEntities()
                    1
                }
                LoadType.PREPEND -> {
                    return@withContext MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with endOfPaginationReached = false because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey = remoteKeys?.nextKey
                        ?: return@withContext MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val mediatorResult = getBooks(page)
            Timber.d("BooksRemoteMediatorImpl loadType=$loadType page=$page result=$mediatorResult")
            return@withContext withContext(Dispatchers.Main.immediate) {
                mediatorResult
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private suspend fun getBooks(page: Int): MediatorResult {
        return when(val networkResult = remoteRepo.getBooks(page)) {
            is ResultState.Success -> {
                MediatorResult.Success(true)
            }
            else -> {
                MediatorResult.Error(Exception())
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, BookItem>): BookRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { book ->
                // Get the remote keys of the last item retrieved
                localRepo.remoteKeysId(book.id)
            }
    }
}