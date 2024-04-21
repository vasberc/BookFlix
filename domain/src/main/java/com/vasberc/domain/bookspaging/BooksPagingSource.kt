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
import org.koin.core.annotation.InjectedParam
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
