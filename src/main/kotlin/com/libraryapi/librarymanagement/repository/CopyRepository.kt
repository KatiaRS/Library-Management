package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.domain.Copy
import org.springframework.data.jpa.repository.JpaRepository

interface CopyRepository : JpaRepository<Copy, Long> {
    fun findByBook(book: Book): List<Copy>
}
