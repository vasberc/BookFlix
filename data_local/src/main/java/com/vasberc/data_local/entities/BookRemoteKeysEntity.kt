package com.vasberc.data_local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vasberc.domain.model.BookRemoteKey
import com.vasberc.domain.model.Domainable

@Entity("book_remote_keys")
data class BookRemoteKeysEntity(
    @PrimaryKey
    val bookId: Int,
    val prevKey: Int?,
    val nextKey: Int?
): Domainable<BookRemoteKey> {
    override fun asDomain(vararg args: Any): BookRemoteKey {
        return BookRemoteKey(bookId, prevKey, nextKey)
    }
}