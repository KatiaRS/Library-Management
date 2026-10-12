package application.book.handlers

data class InvalidISBN(val isbn: ISBN): BookException()
