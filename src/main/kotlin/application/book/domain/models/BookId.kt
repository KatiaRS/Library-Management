package application.book.domain.models

import java.util.UUID
import java.util.UUID.randomUUID

data class BookId(val value: UUID = randomUUID())
