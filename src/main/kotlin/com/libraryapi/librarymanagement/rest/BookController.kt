package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @PostMapping()
    fun create(@RequestBody book: Book): ResponseEntity<Book> {
        return bookService.create(book)
    }

    @GetMapping()
    fun getAll(): List<Book> = bookService.getAll()

    @GetMapping("/{id}")
    fun searchBook(@PathVariable id: UUID): ResponseEntity<Book> {
        return bookService.searchBook(id)
    }

    @GetMapping(params = ["isbn"])
    fun getByIsbn(@RequestParam isbn: String): ResponseEntity<Book> {
        return bookService.getByIsbn(isbn)
    }

    @GetMapping(params = ["title"])
    fun getByTitle(@RequestParam title: String): ResponseEntity<Book> {
        return bookService.getByTitle(title)
    }

    @GetMapping(params = ["author"])
    fun getByAuthor(@RequestParam author: String): ResponseEntity<List<Book>> {
        return bookService.getByAuthor(author)
    }
}
