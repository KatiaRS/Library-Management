package application.book.handlers

import application.book.commands.CreateBookCommand
import application.book.domain.models.Book
import application.book.ports.BookRepository
import jakarta.enterprise.context.ApplicationScoped
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class CreateBookHandlerTest {

    @Test
    fun `should create a valid book from command`() {
        val repo = object : BookRepository {
            private val storage = mutableListOf<Book>()

            override fun save(book: Book): Book {
                storage.add(book)
                return book
            }

            override fun findAll(): List<Book> = storage.toList()

            override fun findById(id: application.book.domain.models.BookId): Book? =
                storage.firstOrNull { it.id == id }
        }

        val handler = CreateBookHandler(repo)
        val command = CreateBookCommand(
            title = "Clean Architecture",
            author = "Robert C. Martin",
            isbn = "9780134494166"
        )

        val created = handler.handle(command)

        assertNotNull(created)
        assertEquals("Clean Architecture", created.title.value)
        assertEquals("Robert C. Martin", created.author.name)
        assertEquals("9780134494166", created.isbn.value)
    }
}

