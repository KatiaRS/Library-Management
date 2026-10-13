package application.user.commands

data class CreateUserCommand(
    val name: String,
    val email: String
)

