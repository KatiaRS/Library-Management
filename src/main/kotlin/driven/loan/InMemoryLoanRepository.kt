package driven.loan
import application.loan.domain.models.Loan
import application.loan.domain.models.LoanId
import application.loan.ports.LoanRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap
@ApplicationScoped
class InMemoryLoanRepository : LoanRepository {
    private val loans = ConcurrentHashMap<LoanId, Loan>()
    override fun save(loan: Loan): Loan {
        loans[loan.id] = loan
        return loan
    }
    override fun findAll(): List<Loan> = loans.values.toList()
    override fun findById(id: LoanId): Loan? = loans[id]
}
