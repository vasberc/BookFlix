package com.vasberc.data_local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao

@Database(
    entities = [],
    version = 1
)
abstract class BookFlixDataBase: RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun bookRemoteKeysDao(): BookRemoteKeysDao
}