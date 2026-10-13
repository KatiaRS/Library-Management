package application.user.domain.models

import java.util.UUID

@JvmInline
value class UserId(val value: UUID) {
    companion object {
        fun new(): UserId = UserId(UUID.randomUUID())
        fun from(value: String): UserId = UserId(UUID.fromString(value))
    }

    override fun toString(): String = value.toString()
}

