package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Fine
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.exception.FineAlreadyPaidException
import com.libraryapi.librarymanagement.exception.FineNotFoundException
import com.libraryapi.librarymanagement.repository.FineRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

@Service
data class FineService(
    val fineRepository: FineRepository,
    val checkoutService: CheckoutService
) {
    fun createFine(loan: Loan): Fine {
        val fineAmountPerDay = BigDecimal(2.99)
        val daysLate = ChronoUnit.DAYS.between(loan.dueDate, loan.returnDate)
        val totalAmountOfTheFine = fineAmountPerDay.multiply(BigDecimal(daysLate))

        val fine = Fine(
            loan = loan,
            amount = totalAmountOfTheFine,
            checkout = checkoutService.createCheckout(
                description = "Multa pelo atraso do empréstimo do livro: ${loan.copy.book.title} ",
                value = totalAmountOfTheFine

            )
        )
        return fineRepository.save(fine)
    }

    fun getByUserId(userId: UUID): List<Fine> {
        return fineRepository.findByLoanUserId(userId)
    }

    fun userHasOpenFine(userId: UUID): Boolean {
        return fineRepository.existsByLoanUserIdAndPaidDateIsNull(userId)
    }

    fun getById(id: UUID): Fine {
        return fineRepository.findById(id).orElseThrow {
            throw FineNotFoundException()
        }
    }

    fun payFine(id: UUID): Fine {
        val fine = getById(id)

        if (fine.isPaid()) {
            throw FineAlreadyPaidException()
        }
        fine.paidDate = LocalDate.now()
        return fineRepository.save(fine)
    }
}
