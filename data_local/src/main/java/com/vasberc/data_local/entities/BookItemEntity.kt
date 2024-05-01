package com.vasberc.data_local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.vasberc.domain.model.BookItem
import com.vasberc.domain.model.Domainable

@Entity("books")
data class BookItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val image: String,
    //Because the room is reordering the elements in the db based on the primary key,
    //we keep the position field to be able to order the items on every load.
    val position: Int
)

data class BookAndAuthorsEntity(
    @Embedded
    val bookItemEntity: BookItemEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val authorEntities: List<AuthorEntity>
): Domainable<BookItem> {
    override fun toDomain(vararg args: Any): BookItem {
        return BookItem(
            bookItemEntity.id,
            bookItemEntity.title,
            authorEntities.map { it.toDomain() },
            bookItemEntity.image
        )
    }

}

fun BookItem.toEntity(position: Int): BookAndAuthorsEntity {
    return BookAndAuthorsEntity(
        bookItemEntity = BookItemEntity(
            id = id,
            title = title,
            image = image,
            position = position
        ),
        authorEntities = authors.map {
            AuthorEntity(
                name = it,
                bookId = id
            )
        }
    )
}

fun BookItem.asRemoteKeyEntity(previous: Int?, next: Int?): BookRemoteKeysEntity {
    return BookRemoteKeysEntity(bookId = id, prevKey = previous, nextKey = next)
}