package application.loan.ports
import application.loan.domain.models.Loan
import application.loan.domain.models.LoanId
interface LoanRepository {
    fun save(loan: Loan): Loan
    fun findAll(): List<Loan>
    fun findById(id: LoanId): Loan?
}
