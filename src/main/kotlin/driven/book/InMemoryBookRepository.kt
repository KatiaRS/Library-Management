package driven.book

import application.book.domain.models.Book
import application.book.domain.models.BookId
import application.book.ports.BookRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class InMemoryBookRepository : BookRepository {
    private val books = ConcurrentHashMap<BookId, Book>()

    override fun save(book: Book): Book {
        books[book.id] = book
        return book
    }

    override fun findAll(): List<Book> = books.values.toList()

    override fun findById(id: BookId): Book? = books[id]
}

