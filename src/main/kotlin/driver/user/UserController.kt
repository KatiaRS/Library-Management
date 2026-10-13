package driver.user
import application.user.commands.CreateUserCommand
import application.user.handlers.CreateUserHandler
import application.user.ports.UserRepository
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
@Path("/users")
class UserController {
    @Inject
    lateinit var createUserHandler: CreateUserHandler
    @Inject
    lateinit var userRepository: UserRepository
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(request: CreateUserRequest): Response {
        val user = createUserHandler.handle(
            CreateUserCommand(
                name = request.name,
                email = request.email
            )
        )
        return Response.status(Response.Status.CREATED).entity(UserResponse.from(user)).build()
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listUsers(): List<UserResponse> = userRepository.findAll().map(UserResponse::from)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun findById(@PathParam("id") id: String): Response {
        val userId = application.user.domain.models.UserId.from(id)
        val user = userRepository.findById(userId) ?: return Response.status(Response.Status.NOT_FOUND).build()
        return Response.ok(UserResponse.from(user)).build()
    }
}
data class CreateUserRequest(
    val name: String,
    val email: String
)
data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val status: String
) {
    companion object {
        fun from(user: application.user.domain.models.User): UserResponse =
            UserResponse(
                id = user.id.toString(),
                name = user.name.value,
                email = user.email.value,
                status = user.status.name
            )
    }
}
