package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.repository.CopyRepository
import com.libraryapi.librarymanagement.rest.dto.BookDto
import com.libraryapi.librarymanagement.rest.dto.convertBookId
import com.libraryapi.librarymanagement.rest.dto.toDto
import com.libraryapi.librarymanagement.rest.dto.toEntity
import com.libraryapi.librarymanagement.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService,
    private val copyRepository: CopyRepository
) {

    @PostMapping()
    fun create(
        @RequestBody @Valid
        bookDto: BookDto
    ): BookDto {
        return bookService.create(bookDto.toEntity()).toDto()
    }

    @PostMapping("/{id}/copy")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addCopy(@PathVariable id: String) {
        bookService.addCopy(convertBookId(id))
    }

    @GetMapping("/{id}/copies")
    fun getAllCopy(@PathVariable id: String): List<Copy> {
        return copyRepository.findByBookId(convertBookId(id))
    }

    @GetMapping
    fun getAll(): List<BookDto> = bookService.getAll().map { it.toDto() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): BookDto {
        return bookService.getById(convertBookId(id)).toDto()
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
        return bookService.getByAuthor(author).map { it.toDto() }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String) = bookService.deleteById(convertBookId(id))

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: String, @RequestBody @Valid bookDto: BookDto): BookDto {
        return bookService.updateById(convertBookId(id), bookDto.toEntity()).toDto()
    }
}
