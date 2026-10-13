package application.book.handlers

import application.book.commands.CreateBookCommand
import application.book.domain.models.Author
import application.book.domain.models.Book
import application.book.domain.models.Isbn
import application.book.domain.models.Title
import application.book.ports.BookRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CreateBookHandler(
    private val bookRepository: BookRepository
) {
    fun handle(command: CreateBookCommand): Book {
        val title = Title.of(command.title)
        val author = Author(command.author)
        val isbn = Isbn.of(command.isbn)

        return bookRepository.save(
            Book.create(
                title = title,
                author = author,
                isbn = isbn
            )
        )
    }
}

