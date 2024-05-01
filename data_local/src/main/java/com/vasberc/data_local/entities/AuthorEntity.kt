package com.vasberc.data_local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vasberc.domain.model.Domainable

@Entity(
    tableName = "authors",
    primaryKeys = ["name", "bookId"],
    //This index helps the performance in case that a book is modified to not scan all table
    //but only the rows with the same bookId
    indices = [Index(value = ["bookId"], unique = false)],
    foreignKeys = [
        ForeignKey(
            entity = BookItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AuthorEntity(
    val name: String,
    val bookId: Int
): Domainable<String> {
    override fun toDomain(vararg args: Any): String {
        return name
    }
}