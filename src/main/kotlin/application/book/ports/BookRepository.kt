package application.book.ports

import application.book.domain.models.Book
import application.book.domain.models.BookId

interface BookRepository {
    fun save(book: Book): Book
    fun findAll(): List<Book>
    fun findById(id: BookId): Book?
}

