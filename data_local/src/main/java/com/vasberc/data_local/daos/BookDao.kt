package com.vasberc.data_local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookAndAuthorsEntity>

    /**
     * With ignore strategy because the item exists in previous page and we want to avoid
     * item reordering animations without user action
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(mapIndexed: List<BookAndAuthorsEntity>)
}