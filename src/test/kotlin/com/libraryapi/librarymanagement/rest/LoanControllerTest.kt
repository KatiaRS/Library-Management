package com.libraryapi.librarymanagement.rest

import application.book.domain.models.Author
import application.book.domain.models.Book
import application.book.domain.models.BookId
import application.book.domain.models.Isbn
import application.book.domain.models.Title
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test

class LoanControllerTest {
    @Test
    fun `should create a valid book before loaning`() {
        assertDoesNotThrow {
            Book.create(
                id = BookId(),
                title = Title.of("Domain-Driven Design"),
                author = Author("Eric Evans"),
                isbn = Isbn.of("9780321125217")
            )
        }
    }
}
