package application.loan.handlers
import application.book.domain.models.BookId
import application.loan.commands.CreateLoanCommand
import application.loan.domain.models.Loan
import application.loan.ports.LoanRepository
import application.user.domain.models.UserId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
class CreateLoanHandlerTest {
    @Test
    fun `should create a valid loan from command`() {
        val repo = object : LoanRepository {
            private val storage = mutableListOf<Loan>()
            override fun save(loan: Loan): Loan {
                storage.add(loan)
                return loan
            }
            override fun findAll(): List<Loan> = storage.toList()
            override fun findById(id: application.loan.domain.models.LoanId): Loan? =
                storage.firstOrNull { it.id == id }
        }
        val handler = CreateLoanHandler(repo)
        val command = CreateLoanCommand(
            bookId = BookId.new().toString(),
            userId = UserId.new().toString()
        )
        val loan = handler.handle(command)
        assertNotNull(loan)
        assertEquals("ACTIVE", loan.status.name)
        assertEquals(command.bookId, loan.bookId.toString())
        assertEquals(command.userId, loan.userId.toString())
    }
}
