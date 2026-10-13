package application.user.domain.models

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EmailTest {

    @Test
    fun `should accept valid email`() {
        assertDoesNotThrow {
            Email.of("maria.silva@email.com")
        }
    }

    @Test
    fun `should reject invalid email without at`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Email.of("maria.silva")
        }

        assertTrue(exception.message!!.contains("E-mail inválido"))
    }

    @Test
    fun `should reject invalid email with invalid domain`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Email.of("maria@dominio")
        }

        assertTrue(exception.message!!.contains("E-mail inválido"))
    }
}
