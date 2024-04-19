package com.vasberc.data_local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookItemEntity

@Database(
    entities = [AuthorEntity::class, BookItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookFlixDataBase: RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun bookRemoteKeysDao(): BookRemoteKeysDao
}