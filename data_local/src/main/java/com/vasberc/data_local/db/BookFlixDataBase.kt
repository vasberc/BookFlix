package com.vasberc.data_local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.data_local.entities.AuthorAndBookDetailedEntity
import com.vasberc.data_local.entities.AuthorDetailedEntity
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.BookRemoteKeysEntity
import com.vasberc.data_local.entities.DetailedBookEntity

@Database(
    entities = [
        AuthorEntity::class, BookItemEntity::class, BookRemoteKeysEntity::class, DetailedBookEntity::class,
        AuthorDetailedEntity::class, AuthorAndBookDetailedEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BookFlixDataBase: RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun bookRemoteKeysDao(): BookRemoteKeysDao
}