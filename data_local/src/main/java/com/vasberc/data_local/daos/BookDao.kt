package com.vasberc.data_local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.vasberc.data_local.entities.AuthorAndBookDetailedEntity
import com.vasberc.data_local.entities.AuthorDetailedEntity
import com.vasberc.data_local.entities.AuthorEntity
import com.vasberc.data_local.entities.BookAndAuthorsEntity
import com.vasberc.data_local.entities.BookDetailedWithRelations
import com.vasberc.data_local.entities.BookItemEntity
import com.vasberc.data_local.entities.DetailedBookEntity
import timber.log.Timber

@Dao
interface BookDao {

    @Transaction
    @Query("SELECT * FROM books ORDER BY position LIMIT :limit OFFSET :offset")
    suspend fun getBooksByPage(limit: Int, offset: Int): List<BookAndAuthorsEntity>

    @Query("DELETE FROM books")
    suspend fun clearAllEntities()

    @Transaction
    @Query("SELECT * FROM books ORDER BY position")
    suspend fun getAllBooks(): List<BookAndAuthorsEntity>

    @Transaction
    suspend fun insertAllBookAndAuthors(bookAndAuthorsEntities: List<BookAndAuthorsEntity>, isRefresh: Boolean) {
        if(isRefresh) {
            clearAllEntities()
        }
        val books = mutableListOf<BookItemEntity>()
        val authors = mutableListOf<AuthorEntity>()
        //Do it with forEach to because the bookAndAuthorsEntities.map { it.bookItemEntity }
        //should be done twice (for books and authors) which means forEach 2 times
        bookAndAuthorsEntities.forEach {
            books.add(it.bookItemEntity)
            authors.addAll(it.authorEntities)
        }

        insertAllBooks(books)
        insertAllAuthors(authors)
    }

    /**
     * With ignore strategy because the item exists in previous page and we want to avoid
     * item reordering animations without user action
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllBooks(bookEntities: List<BookItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllAuthors(authorEntities: List<AuthorEntity>)

    @Transaction
    @Query("SELECT * FROM detailed_book WHERE id = :bookId")
    suspend fun getDetailedBook(bookId: Int): BookDetailedWithRelations?

    @Transaction
    @Query("SELECT * FROM detailed_book")
    suspend fun getDetailedBooks(): List<BookDetailedWithRelations>

    @Query("DELETE FROM detailed_book WHERE id = :bookId")
    suspend fun deleteDetailedBook(bookId: Int)

    @Query("DELETE FROM author_book_detailed WHERE bookId = :bookId")
    suspend fun deleteDetailedAuthorAndBook(bookId: Int)

    @Query("DELETE FROM author_detailed WHERE name = :name")
    suspend fun deleteDetailedAuthor(name: String)

    @Transaction
    suspend fun insertDetailedBook(bookDetailedWithRelations: BookDetailedWithRelations) {
        val books = getDetailedBooks()
        //Cache max 200 books
        if(books.size >= 200) {
            //Note, this is not the oldest book because the sqlite reorders the rows based the primary key
            //but for this assignment we can pretend that this is the oldest book
            val last = books.first()
            deleteDetailedBook(last.bookDetailed.id)
            deleteDetailedAuthorAndBook(last.bookDetailed.id)
            last.authors.forEach {
                try {
                    deleteDetailedAuthor(it.name)
                } catch (e: Exception) {
                    //Exception thrown, this author has relation also with other book
                    Timber.w(e)
                }
            }

        }

        insertDetailedBook(bookDetailedWithRelations.bookDetailed)
        bookDetailedWithRelations.authors.forEach {
            insertAuthorDetailed(it)
            insertBookAndAuthorDetailed(AuthorAndBookDetailedEntity(it.name, bookDetailedWithRelations.bookDetailed.id))
        }

    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetailedBook(book: DetailedBookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthorDetailed(author: AuthorDetailedEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookAndAuthorDetailed(authorAndBook: AuthorAndBookDetailedEntity)
}