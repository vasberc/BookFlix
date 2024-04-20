package com.vasberc.data_local.daos

import androidx.room.Dao
import androidx.room.Query
import com.vasberc.data_local.entities.BookRemoteKeysEntity

@Dao
interface BookRemoteKeysDao {
    @Query("DELETE FROM book_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM book_remote_keys WHERE bookId = :bookId")
    suspend fun getRemoteKeyById(bookId: Int): BookRemoteKeysEntity?

    @Query("SELECT * FROM book_remote_keys")
    suspend fun getAllKeys(): List<BookRemoteKeysEntity>
}