package application.user.ports
import application.user.domain.models.User
import application.user.domain.models.UserId
interface UserRepository {
    fun save(user: User): User
    fun findAll(): List<User>
    fun findById(id: UserId): User?
}
