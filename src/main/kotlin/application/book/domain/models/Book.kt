package application.book.domain.models

data class Book(
    val id: BookId,
    val title: Title,
    val author: Author,
    val isbn: Isbn
)
