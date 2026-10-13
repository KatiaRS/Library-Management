package application.user.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `should create active user`() {
        val user = User.create(
            name = UserName.of("Maria Silva"),
            email = Email.of("maria.silva@email.com")
        )

        assertEquals(UserStatus.ACTIVE, user.status)
        assertTrue(user.isActive())
    }

    @Test
    fun `should deactivate user`() {
        val user = User.create(
            name = UserName.of("Maria Silva"),
            email = Email.of("maria.silva@email.com")
        )

        user.deactivate()

        assertEquals(UserStatus.INACTIVE, user.status)
        assertFalse(user.isActive())
    }
}
