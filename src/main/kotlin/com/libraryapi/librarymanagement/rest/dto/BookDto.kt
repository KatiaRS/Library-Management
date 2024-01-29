package com.libraryapi.librarymanagement.rest.dto

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.exception.ConversionIdException
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

const val BOOK_PREFIX = "BOOK_"
data class BookDto(
    val id: String?,

    @field:NotBlank
    @field:Size(max = 255)
    val title: String?,

    @field:NotBlank
    @field:Size(max = 255)
    val author: String?,

    @field:NotBlank
    @field:Size(min = 10, max = 13)
    val isbn: String?
)

fun BookDto.toEntity(): Book = Book(
    id = null,
    title = this.title!!,
    author = this.author!!,
    isbn = this.isbn!!
)
fun Book.toDto(): BookDto = BookDto(
    id = addBookPrefix(id!!),
    title = this.title,
    author = this.author,
    isbn = this.isbn
)
fun convertBookId(id: String): UUID {
    val noPrefix = id.removePrefix(BOOK_PREFIX)
    try {
        return UUID.fromString(noPrefix)
    } catch (e: Exception) {
        throw ConversionIdException()
    }
}

fun addBookPrefix(id: UUID) = "$BOOK_PREFIX$id"
