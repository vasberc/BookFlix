package com.vasberc.data_local.di

import android.content.Context
import androidx.room.Room
import com.vasberc.data_local.daos.BookDao
import com.vasberc.data_local.daos.BookRemoteKeysDao
import com.vasberc.data_local.db.BookFlixDataBase
import com.vasberc.domain.di.DomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [DomainModule::class])
@ComponentScan("com.vasberc.data_local")
class DataLocalModule

@Single
fun provideDb(context: Context): BookFlixDataBase {
    return Room.databaseBuilder(
        context = context,
        klass = BookFlixDataBase::class.java,
        name = "book_flix_db"
    ).build()
}

@Single
fun provideBookDao(dataBase: BookFlixDataBase): BookDao {
    return dataBase.bookDao()
}

@Single
fun provideBookRemoteKeysDao(dataBase: BookFlixDataBase): BookRemoteKeysDao {
    return dataBase.bookRemoteKeysDao()
}
