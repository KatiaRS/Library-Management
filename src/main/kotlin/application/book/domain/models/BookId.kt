package application.book.domain.models

import java.util.UUID
import java.util.UUID.randomUUID

data class BookId(val value: UUID = randomUUID()) {
    companion object {
        fun new(): BookId = BookId()
        fun from(value: String): BookId = BookId(UUID.fromString(value))
    }
}
