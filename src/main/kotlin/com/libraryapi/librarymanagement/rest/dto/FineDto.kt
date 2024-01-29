package com.libraryapi.librarymanagement.rest.dto

import com.libraryapi.librarymanagement.domain.Fine
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

const val FINE_PREFIX = "FINE_"
class FineDto(
    val id: String,
    var loan: String,
    val paidDate: LocalDate? = null,
    val amount: BigDecimal = BigDecimal.ZERO,
    val paid: Boolean
)
fun Fine.toDto() = FineDto(
    id = addFinePrefix(id!!),
    loan = addLoanPrefix(loan.id!!),
    paidDate = paidDate,
    amount = amount,
    paid = isPaid()

)
fun addFinePrefix(id: UUID) = "$FINE_PREFIX$id"
