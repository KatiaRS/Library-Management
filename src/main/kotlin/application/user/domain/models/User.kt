package application.user.domain.models
class User private constructor(
    val id: UserId,
    val name: UserName,
    val email: Email,
    var status: UserStatus
) {
    fun activate() {
        status = UserStatus.ACTIVE
    }
    fun deactivate() {
        status = UserStatus.INACTIVE
    }
    fun isActive(): Boolean = status == UserStatus.ACTIVE
    companion object {
        fun create(name: UserName, email: Email, id: UserId = UserId.new()): User =
            User(id, name, email, UserStatus.ACTIVE)
    }
}
