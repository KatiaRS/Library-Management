package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.exception.NoCopiesAvailableException
import com.libraryapi.librarymanagement.exception.UserReachedLoanLimitException
import com.libraryapi.librarymanagement.repository.CopyRepository
import com.libraryapi.librarymanagement.repository.LoanRepository
import org.springframework.stereotype.Service
import java.util.*

const val MAX_LOANS_PER_USER = 5

@Service
class LoanService(
    private val userService: UserService,
    private val copyRepository: CopyRepository,
    private val loanRepository: LoanRepository,
    private val bookService: BookService
) {
    fun create(loan: Loan): Loan {
        loan.user = userService.getById(loan.user.id!!)
        loan.copy = getCopyAvailable(loan.copy.book.id!!)

        if (!canBorrowMoreBooks(loan.user.id!!)) {
            throw UserReachedLoanLimitException()
        }

        return loanRepository.save(loan)
    }
    private fun canBorrowMoreBooks(userId: UUID): Boolean {
        // Logica para verificar se o usuário poderá emprestar mais livros
        // e se não tem multas ou empréstimos atrasados
        val activeLoans = loanRepository.countByUserIdAndReturnDateIsNull(userId)
        return activeLoans < MAX_LOANS_PER_USER
    }

    private fun getCopyAvailable(bookId: UUID): Copy {
        // logica para verificar se há livros disponíveis para empréstimo

        val book = bookService.getById(bookId)
        val copies = copyRepository.findAvailableCopies(bookId)

        if (copies.isEmpty()) {
            throw NoCopiesAvailableException()
        }

        return copies.first()
    }
}
