package com.libraryapi.librarymanagement

import application.book.domain.models.Author
import application.book.domain.models.Book
import application.book.domain.models.BookId
import application.book.domain.models.Isbn
import application.book.domain.models.Title
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class LibraryManagementApplicationTests {

    @Test
    fun `should build a valid book aggregate`() {
        val title = Title.of("Clean Architecture")
        val author = Author("Robert C. Martin")
        val isbn = Isbn.of("9780134494166")

        val book = Book.create(title = title, author = author, isbn = isbn, id = BookId())

        assertEquals(title, book.title)
        assertEquals(author, book.author)
        assertEquals(isbn, book.isbn)
    }

    @Test
    fun `should reject invalid isbn`() {
        assertThrows(RuntimeException::class.java) {
            Isbn.of("123")
        }
    }
}
