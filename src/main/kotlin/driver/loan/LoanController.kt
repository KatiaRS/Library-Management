package driver.loan
import application.loan.commands.CreateLoanCommand
import application.loan.handlers.CreateLoanHandler
import application.loan.ports.LoanRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
@ApplicationScoped
@Path("/loans")
class LoanController {
    @Inject
    lateinit var createLoanHandler: CreateLoanHandler
    @Inject
    lateinit var loanRepository: LoanRepository
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createLoan(request: CreateLoanRequest): Response {
        val loan = createLoanHandler.handle(
            CreateLoanCommand(
                bookId = request.bookId,
                userId = request.userId
            )
        )
        return Response.status(Response.Status.CREATED).entity(LoanResponse.from(loan)).build()
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listLoans(): List<LoanResponse> = loanRepository.findAll().map(LoanResponse::from)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun findById(@PathParam("id") id: String): Response {
        val loanId = application.loan.domain.models.LoanId.from(id)
        val loan = loanRepository.findById(loanId) ?: return Response.status(Response.Status.NOT_FOUND).build()
        return Response.ok(LoanResponse.from(loan)).build()
    }
}
data class CreateLoanRequest(
    val bookId: String,
    val userId: String
)
data class LoanResponse(
    val id: String,
    val bookId: String,
    val userId: String,
    val status: String,
    val borrowedAt: String
) {
    companion object {
        fun from(loan: application.loan.domain.models.Loan): LoanResponse =
            LoanResponse(
                id = loan.id.toString(),
                bookId = loan.bookId.value.toString(),
                userId = loan.userId.toString(),
                status = loan.status.name,
                borrowedAt = loan.borrowedAt.toString()
            )
    }
}
