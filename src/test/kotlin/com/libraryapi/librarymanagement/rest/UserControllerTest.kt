package com.libraryapi.librarymanagement.rest

import application.book.domain.models.Author
import application.book.domain.models.Book
import application.book.domain.models.BookId
import application.book.domain.models.Isbn
import application.book.domain.models.Title
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class UserControllerTest {
    @Test
    fun `should build a valid book domain object`() {
        val book = Book.create(
            id = BookId(),
            title = Title.of("Refactoring"),
            author = Author("Martin Fowler"),
            isbn = Isbn.of("9780201485677")
        )
        assertNotNull(book)
    }
}
