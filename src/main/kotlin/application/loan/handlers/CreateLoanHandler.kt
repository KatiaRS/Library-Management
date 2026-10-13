package application.loan.handlers
import application.book.domain.models.BookId
import application.loan.commands.CreateLoanCommand
import application.loan.domain.models.Loan
import application.loan.ports.LoanRepository
import application.user.domain.models.UserId
import jakarta.enterprise.context.ApplicationScoped
@ApplicationScoped
class CreateLoanHandler(
    private val loanRepository: LoanRepository
) {
    fun handle(command: CreateLoanCommand): Loan {
        val loan = Loan.create(
            bookId = BookId.from(command.bookId),
            userId = UserId.from(command.userId)
        )
        return loanRepository.save(loan)
    }
}
