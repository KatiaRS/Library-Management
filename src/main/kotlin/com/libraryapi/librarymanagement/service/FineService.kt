package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Fine
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.repository.FineRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.temporal.ChronoUnit
import java.util.*
import javax.xml.datatype.DatatypeConstants.DAYS

@Service
data class FineService(
    val fineRepository: FineRepository
) {
    fun createFine(loan: Loan): Fine {
        // para gerar uma multa é preciso calcular quantos dias o usuário está em atraso
        // para isso, vemos o dia de retono com a data de entrega

        val fineAmountPerDay = BigDecimal(2.99)
        val daysLate = ChronoUnit.DAYS.between(loan.dueDate, loan.returnDate)
        val totalAmountOfTheFine = fineAmountPerDay.multiply(BigDecimal(daysLate))

        val fine = Fine(
            loan = loan,
            amount = totalAmountOfTheFine

        )
        return fineRepository.save(fine)
    }

    fun getById(userId: UUID): List<Fine> {
        return fineRepository.findAll()
    }
}
