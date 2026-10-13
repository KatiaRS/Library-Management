package driven.user
import application.user.domain.models.User
import application.user.domain.models.UserId
import application.user.ports.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap
@ApplicationScoped
class InMemoryUserRepository : UserRepository {
    private val users = ConcurrentHashMap<UserId, User>()
    override fun save(user: User): User {
        users[user.id] = user
        return user
    }
    override fun findAll(): List<User> = users.values.toList()
    override fun findById(id: UserId): User? = users[id]
}
