package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.Copy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface CopyRepository : JpaRepository<Copy, Long> {
    fun findByBookId(id: UUID): List<Copy>

    @Query("select c from Copy c where c.book.id = :bookId and not exists (select 1 from Loan l where l.returnDate = null and c.id = l.copy.id)")
    fun findAvailableCopies(bookId: UUID): List<Copy>
}
