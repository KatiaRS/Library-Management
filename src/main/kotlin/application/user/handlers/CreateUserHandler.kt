package application.user.handlers
import application.user.commands.CreateUserCommand
import application.user.domain.models.Email
import application.user.domain.models.User
import application.user.domain.models.UserName
import application.user.ports.UserRepository
import jakarta.enterprise.context.ApplicationScoped
@ApplicationScoped
class CreateUserHandler(
    private val userRepository: UserRepository
) {
    fun handle(command: CreateUserCommand): User {
        val user = User.create(
            name = UserName.of(command.name),
            email = Email.of(command.email)
        )
        return userRepository.save(user)
    }
}
