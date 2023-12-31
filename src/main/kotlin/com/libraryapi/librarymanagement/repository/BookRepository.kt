package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookRepository : JpaRepository<Book, UUID> {
    fun findByTitle(title: String): Book?
    fun findByAuthor(author: String): List<Book>
    fun findByIsbn(isbn: String): Book?
    fun existsByIsbnOrTitle(isbn: String, title: String): Boolean
}
