package application.loan.domain.models
import application.book.domain.models.BookId
import application.user.domain.models.UserId
import java.time.Instant
class Loan private constructor(
    val id: LoanId,
    val bookId: BookId,
    val userId: UserId,
    var status: LoanStatus,
    val borrowedAt: Instant,
    var returnedAt: Instant? = null
) {
    fun markReturned() {
        require(status != LoanStatus.RETURNED) { "Loan already returned" }
        status = LoanStatus.RETURNED
        returnedAt = Instant.now()
    }
    fun markOverdue() {
        status = LoanStatus.OVERDUE
    }
    companion object {
        fun create(
            bookId: BookId,
            userId: UserId,
            id: LoanId = LoanId.new()
        ): Loan = Loan(
            id = id,
            bookId = bookId,
            userId = userId,
            status = LoanStatus.ACTIVE,
            borrowedAt = Instant.now()
        )
    }
}
