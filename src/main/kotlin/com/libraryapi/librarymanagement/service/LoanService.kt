package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.exception.BookAlreadyBeenReturnedException
import com.libraryapi.librarymanagement.exception.LoanNotFoundException
import com.libraryapi.librarymanagement.exception.NoCopiesAvailableException
import com.libraryapi.librarymanagement.exception.UserHaveFineException
import com.libraryapi.librarymanagement.exception.UserReachedLoanLimitException
import com.libraryapi.librarymanagement.repository.CopyRepository
import com.libraryapi.librarymanagement.repository.LoanRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

const val MAX_LOANS_PER_USER = 5

@Service
class LoanService(
    private val userService: UserService,
    private val copyRepository: CopyRepository,
    private val loanRepository: LoanRepository,
    private val fineService: FineService
) {
    fun create(loan: Loan): Loan {
        loan.user = userService.getById(loan.user.id!!)
        loan.copy = getCopyAvailable(loan.copy.book.id!!)

        if (!canBorrowMoreBooks(loan.user.id!!)) {
            throw UserReachedLoanLimitException()
        }

        if (fineService.userHasOpenFine(loan.user.id!!)) {
            throw UserHaveFineException()
        }
        return loanRepository.save(loan)
    }

    fun getById(id: UUID): Loan {
        return loanRepository.findById(id).orElseThrow {
            throw LoanNotFoundException()
        }
    }

    fun devLoan(id: UUID): Loan? {
        val loan = getById(id)
        if (loan.returnDate != null) {
            throw BookAlreadyBeenReturnedException()
        }
        loan.returnDate = LocalDate.now()
        return loanRepository.save(loan).also {
            if (it.returnDate!!.isAfter(it.dueDate)) {
                it.apply {
                    fine = fineService.createFine(it)
                }
            }
        }
    }

    private fun canBorrowMoreBooks(userId: UUID): Boolean {
        val activeLoans = loanRepository.countByUserIdAndReturnDateIsNull(userId)
        return activeLoans < MAX_LOANS_PER_USER
    }

    private fun getCopyAvailable(bookId: UUID): Copy {
        val copies = copyRepository.findAvailableCopies(bookId)

        if (copies.isEmpty()) {
            throw NoCopiesAvailableException()
        }

        return copies.first()
    }
}
