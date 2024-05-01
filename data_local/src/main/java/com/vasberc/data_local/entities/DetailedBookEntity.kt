package com.vasberc.data_local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.vasberc.domain.model.BookDetailed
import com.vasberc.domain.model.Domainable

@Entity("detailed_book")
data class DetailedBookEntity(
    @PrimaryKey
    val id: Int,
    val subject: String,
    val image: String,
    val title: String
)

@Entity("author_detailed")
data class AuthorDetailedEntity(
    @PrimaryKey
    val name: String,
    val birthYear: Int,
    val deathYear: Int
): Domainable<BookDetailed.Author> {
    override fun toDomain(vararg args: Any): BookDetailed.Author {
        return BookDetailed.Author(
            name = name, birthYear = birthYear, deathYear = deathYear
        )
    }

}

@Entity(
    tableName = "author_book_detailed",
    primaryKeys = ["name", "bookId"],
    foreignKeys = [
        ForeignKey(
            entity = DetailedBookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AuthorDetailedEntity::class,
            parentColumns = ["name"],
            childColumns = ["name"],
            onUpdate = ForeignKey.CASCADE,
            //Restrict deletion of AuthorDetailedEntity, because there is book with this author
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["bookId"], unique = false),
        Index(value = ["name"], unique = false)
    ]
)
data class AuthorAndBookDetailedEntity(
    val name: String,
    val bookId: Int
)

data class BookDetailedWithRelations(
    @Embedded
    val bookDetailed: DetailedBookEntity,
    @Relation(
        parentColumn = "id",
        entity = AuthorDetailedEntity::class,
        entityColumn = "name",
        associateBy = Junction(
            value = AuthorAndBookDetailedEntity::class,
            parentColumn = "bookId",
            entityColumn = "name"
        )
    )
    val authors: List<AuthorDetailedEntity>
): Domainable<BookDetailed> {
    override fun toDomain(vararg args: Any): BookDetailed {
        return BookDetailed(
            id = bookDetailed.id,
            authors = authors.map { it.toDomain() },
            subject = bookDetailed.subject,
            image = bookDetailed.image,
            title = bookDetailed.title
        )
    }

}

fun BookDetailed.toEntity(): BookDetailedWithRelations {
    return BookDetailedWithRelations(
        bookDetailed = DetailedBookEntity(
            id = id, subject = subject, image = image, title = title
        ),
        authors = authors.map { it.toEntity() }
    )
}

fun BookDetailed.Author.toEntity(): AuthorDetailedEntity {
    return AuthorDetailedEntity(
        name = name, birthYear = birthYear, deathYear = deathYear
    )
}

