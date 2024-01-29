package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.Loan
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LoanRepository : JpaRepository<Loan, UUID> {

    fun countByUserIdAndReturnDateIsNull(userId: UUID): Int
}
