package application.book.commands

data class CreateBookCommand(
    val title: String,
    val author: String,
    val isbn: String
)

