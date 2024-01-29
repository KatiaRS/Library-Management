package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.Fine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FineRepository : JpaRepository<Fine, UUID> {

    fun findByLoanUserId(userId: UUID): List<Fine>

    fun existsByLoanUserIdAndPaidDateIsNull(userId: UUID): Boolean
}
