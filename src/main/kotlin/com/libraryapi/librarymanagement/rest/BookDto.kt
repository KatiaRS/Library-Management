package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.domain.Book
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
    id = "$BOOK_PREFIX$id",
    title = this.title,
    author = this.author,
    isbn = this.isbn
)



