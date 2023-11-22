package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.repository.BookRepository
import com.libraryapi.librarymanagement.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService, private val bookRepository: BookRepository) {

    @PostMapping()
    fun create(@RequestBody @Valid bookDto: BookDto): BookDto {
        return bookService.create(bookDto.toEntity()).toDto()
    }

    @GetMapping()
    fun getAll(): List<BookDto> = bookService.getAll().map { it.toDto() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): BookDto {
        return bookService.getById(convertId(id)).toDto()
    }

    @GetMapping(params = ["isbn"])
    fun getByIsbn(@RequestParam isbn: String): BookDto {
        return bookService.getByIsbn(isbn).toDto()
    }


    @GetMapping(params = ["title"])
    fun getByTitle(@RequestParam title: String): BookDto {
        return bookService.getByTitle(title).toDto()
    }

    @GetMapping(params = ["author"])
    fun getByAuthor(@RequestParam author: String): List<BookDto> {
        return (bookService.getByAuthor(author).map { it.toDto() })
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String) = this.bookService.deleteById(convertId(id))

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: String, @RequestBody @Valid bookDto: BookDto): BookDto {
        return bookService.updateById(convertId(id), bookDto.toEntity()).toDto()
    }

    private fun convertId(id: String): UUID {
        val semPrefix = id.removePrefix(BOOK_PREFIX)
        try {
            return UUID.fromString(semPrefix)
        } catch (e: Exception) {
            throw ConversionIdException()
        }


    }
}

