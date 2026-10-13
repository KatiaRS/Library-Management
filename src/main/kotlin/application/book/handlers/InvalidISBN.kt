package application.book.handlers

import application.book.domain.models.Isbn

class InvalidISBN(val isbn: Isbn) : BookException("ISBN inválido: ${isbn.value}")
