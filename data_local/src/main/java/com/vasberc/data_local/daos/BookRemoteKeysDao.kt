package com.vasberc.data_local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

    /**
     * With ignore strategy because the same strategy applied to the book it self
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(map: List<BookRemoteKeysEntity>)
}