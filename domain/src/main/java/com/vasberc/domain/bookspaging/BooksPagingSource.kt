package com.vasberc.domain.bookspaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.repo.BooksLocalRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import timber.log.Timber

abstract class BooksPagingSource(
    db: RoomDatabase,
    protected val localRepo: BooksLocalRepo
): PagingSource<Int, BookItem>() {

    init {
        db.invalidationTracker.addObserver(object : InvalidationTracker.Observer(arrayOf("books")) {
            override fun onInvalidated(tables: Set<String>) {
                invalidate()
                //Remove this observer because when the invalidate is invoked pager creates a new instance of the paging source
                db.invalidationTracker.removeObserver(this)
            }
        })
    }
}

@Factory
class BooksPagingSourceImpl(
    db: RoomDatabase,
    localRepo: BooksLocalRepo,
    remoteMediator: BooksRemoteMediator
): BooksPagingSource(db, localRepo) {

    private val remoteDataTotalItems: Int? = remoteMediator.remoteDataTotalItems
    override fun getRefreshKey(state: PagingState<Int, BookItem>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookItem> {
        return try {
            val page = params.key ?: 1
            val (books, itemsBefore, itemsAfter) = withContext(Dispatchers.IO) {
                val limit = params.loadSize
                val offset = (page - 1) * limit
                val books = localRepo.getBooksByPage(limit, offset)
                val itemsBefore = params.loadSize * (page -1)
                //Setting items after to 20 if the total items is null, because
                //null means that is the initial load of the list, and we will insert 100 loading place holders
                //tha they will display the shimmer loading item.
                //Also setting maximum value of items after to 20(page size), in case of loading error, user cannot scroll
                //to the next page
                val itemsAfter = ((remoteDataTotalItems ?: 20) - itemsBefore - books.size).coerceAtMost(20)
                Triple(books, itemsBefore, itemsAfter)
            }
            Timber.d("results $books")
            LoadResult.Page(
                data = books,
                prevKey = if(page == 1) null else page - 1,
                nextKey = if(books.isEmpty()) null else page + 1,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}