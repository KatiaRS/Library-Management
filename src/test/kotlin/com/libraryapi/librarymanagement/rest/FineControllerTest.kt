package com.libraryapi.librarymanagement.rest
import application.book.domain.models.Isbn
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
class FineControllerTest {
    @Test
    fun `should accept a valid isbn`() {
        assertDoesNotThrow { Isbn.of("9780134494166") }
    }
}
