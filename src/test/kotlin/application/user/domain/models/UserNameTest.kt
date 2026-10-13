package application.user.domain.models

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserNameTest {

    @Test
    fun `should accept valid user name`() {
        assertDoesNotThrow {
            UserName.of("Maria Silva")
        }
    }

    @Test
    fun `should reject invalid user name with numbers`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            UserName.of("Maria123")
        }

        assertTrue(exception.message!!.contains("Nome inválido"))
    }

    @Test
    fun `should reject invalid user name with special characters`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            UserName.of("Maria@Silva")
        }

        assertTrue(exception.message!!.contains("Nome inválido"))
    }
}
