package com.libraryapi.librarymanagement.rest

import application.book.domain.models.Author
import application.book.domain.models.Book
import application.book.domain.models.BookId
import application.book.domain.models.Isbn
import application.book.domain.models.Title
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BookControllerTest {
    @Test
    fun `should create valid book aggregate`() {
        val title = Title.of("The Pragmatic Programmer")
        val author = Author("Andrew Hunt")
        val isbn = Isbn.of("9780201616224")
        val book = Book.create(title = title, author = author, isbn = isbn, id = BookId())
        assertEquals(title, book.title)
        assertEquals(author, book.author)
        assertEquals(isbn, book.isbn)
    }
}
