package application.loan.domain.models
import java.util.UUID
@JvmInline
value class LoanId(val value: UUID) {
    companion object {
        fun new(): LoanId = LoanId(UUID.randomUUID())
        fun from(value: String): LoanId = LoanId(UUID.fromString(value))
    }
    override fun toString(): String = value.toString()
}
