package com.vasberc.data_local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookItemEntity

@Dao
interface BookDao {

    @Transaction
    @Query("SELECT * FROM books ORDER BY position LIMIT :limit OFFSET :offset")
    suspend fun getBooksByPage(limit: Int, offset: Int): List<BookAndAuthorsEntity>

    @Query("DELETE FROM books")
    suspend fun clearAllEntities()

    @Transaction
    @Query("SELECT * FROM books ORDER BY position")
    suspend fun getAllBooks(): List<BookAndAuthorsEntity>

    @Transaction
    suspend fun insertAllBookAndAuthors(bookAndAuthorsEntities: List<BookAndAuthorsEntity>, isRefresh: Boolean) {
        if(isRefresh) {
            clearAllEntities()
        }
        val books = mutableListOf<BookItemEntity>()
        val authors = mutableListOf<AuthorEntity>()
        //Do it with forEach to because the bookAndAuthorsEntities.map { it.bookItemEntity }
        //should be done twice (for books and authors) which means forEach 2 times
        bookAndAuthorsEntities.forEach {
            books.add(it.bookItemEntity)
            authors.addAll(it.authorEntities)
        }

        insertAllBooks(books)
        insertAllAuthors(authors)
    }

    /**
     * With ignore strategy because the item exists in previous page and we want to avoid
     * item reordering animations without user action
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllBooks(bookEntities: List<BookItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllAuthors(authorEntities: List<AuthorEntity>)
}