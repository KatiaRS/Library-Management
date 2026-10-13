package application.loan.commands
data class CreateLoanCommand(
    val bookId: String,
    val userId: String
)
