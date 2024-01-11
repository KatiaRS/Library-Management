package com.libraryapi.librarymanagement.rest.dto

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.domain.User
import com.libraryapi.librarymanagement.exception.ConversionIdException
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.util.*

const val LOAN_PREFIX = "LOAN_"

data class LoanDto(
    var id: String? = null,

    @field:NotBlank
    val userId: String?,

    @field:NotBlank
    val bookId: String?,
    var issuedDate: LocalDate? = null,
    var dueDate: LocalDate? = null,
    var returnedDate: LocalDate? = null
)

fun LoanDto.toEntity(): Loan = Loan(
    user = User(convertUserId(userId!!)),
    copy = Copy(book = Book(convertBookId(bookId!!)))
)

fun Loan.toDto(): LoanDto = LoanDto(
    id = addLoanPrefix(id!!),
    userId = addUserPrefix(user.id!!),
    bookId = addBookPrefix(copy.book.id!!),
    issuedDate = this.issuedDate,
    dueDate = this.dueDate
)
fun convertLoanId(id: String): UUID {
    val noPrefix = id.removePrefix(LOAN_PREFIX)
    try {
        return UUID.fromString(noPrefix)
    } catch (e: Exception) {
        throw ConversionIdException()
    }
}

fun addLoanPrefix(id: UUID) = "$LOAN_PREFIX$id"
