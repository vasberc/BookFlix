package com.vasberc.data_local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookItemEntity

@Dao
interface BookDao {

    @Transaction
    @Query("SELECT * FROM books LIMIT :limit OFFSET :offset")
    suspend fun getBooksByPage(limit: Int, offset: Int): List<BookAndAuthorsEntity>

    @Query("DELETE FROM books")
    suspend fun clearAllEntities()

    @Transaction
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<BookAndAuthorsEntity>
}