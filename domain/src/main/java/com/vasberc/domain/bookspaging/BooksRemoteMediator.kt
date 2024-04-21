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

    /**
     * In each refresh will hold the previous cached items in order to
     * recover them again if the refresh fails due to connection errors
     */
    private var cachedBooks: List<BookItem>? = null

    /**
     * Pairs page to starting index
     */
    private var startingIndexOfPage = hashMapOf(1 to 1)

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
                    startingIndexOfPage = hashMapOf(1 to 1)
                    cachedBooks = localRepo.getAllBooks()
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

            val mediatorResult = loadPage(page, loadType)
            Timber.d("BooksRemoteMediatorImpl loadType=$loadType page=$page result=$mediatorResult")
            return@withContext withContext(Dispatchers.Main.immediate) {
                mediatorResult
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private suspend fun loadPage(page: Int, loadType: LoadType): MediatorResult {
        return when(val networkResult = remoteRepo.getBooks(page)) {
            is ResultState.Success -> {
                handleSuccessRefresh(loadType)
                remoteDataTotalItems = networkResult.data.totalItems
                if(remoteDataTotalItems == 0) {
                    MediatorResult.Success(true)
                } else {
                    val books = networkResult.data.currentPageItems
                    insertItemsToDb(books, page, books.isEmpty(), loadType == LoadType.REFRESH)
                    //If the books are empty means that the previous page was the last page,
                    //so we need to stop fetching next page
                    MediatorResult.Success(books.isEmpty())
                }

            }
            else -> {
                handleErrorRefresh(loadType)
                //Return also exception to handle it on the ui to make some automatic retries
                MediatorResult.Error(Exception("loadType=${loadType.name}"))
            }
        }
    }

    private suspend fun handleErrorRefresh(loadType: LoadType) {
        if(loadType == LoadType.REFRESH) {
            //Declaring the total items with 20 items plus will make the paging source to have the itemsAfter
            //at the end of the list 20 and the ui will display 20 loading items.
            //this way the user will understand that there is an issue and will try to refresh again the list.
            remoteDataTotalItems = cachedBooks?.size?.plus(20) ?: 20
            cachedBooks?.let {
                insertItemsToDb(it, 1, true, true)
            }
            //No need em any more, release the memory
            cachedBooks = null

        }
    }

    private fun handleSuccessRefresh(loadType: LoadType) {
        if(loadType == LoadType.REFRESH ) {
            //Release the cached books, because the refresh succeeded and the new data from the network
            //will be used.
            cachedBooks = null
        }
    }

    private suspend fun insertItemsToDb(books: List<BookItem>, page: Int, endOfPaginationReached: Boolean, isRefresh: Boolean) {
        val prevPage = (page - 1).takeIf { it > 0 }
        val nextPage = (page + 1).takeIf { !endOfPaginationReached }
        localRepo.insertAllBookRemoteKeys(books, prevPage, nextPage, isRefresh)
        localRepo.insertAllBooks(books, startingIndexOfPage[page]!!, isRefresh)
        //Calc the starting index to be able to use it on the next page's load
        startingIndexOfPage[page + 1] = startingIndexOfPage[page]!! + books.size
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, BookItem>): BookRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { book ->
                // Get the remote keys of the last item retrieved
                localRepo.getRemoteKeyById(book.id)
            }
    }
}