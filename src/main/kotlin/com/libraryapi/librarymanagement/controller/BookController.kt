package com.libraryapi.librarymanagement.controller

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.repository.BookRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/books")
class BookController(private val repository: BookRepository) {

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)

    fun create(@RequestBody book: Book): ResponseEntity<Book> {
        if (repository.existsByIsbnOrTitle(book.isbn, book.title)) {
            return ResponseEntity<Book>(HttpStatus.BAD_REQUEST)
        }
        val savedBook = repository.save(book)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook)
    }


    @GetMapping()
    fun getAll(): List<Book> = repository.findAll()

    @GetMapping("/{id}")
    fun searchBook(@PathVariable id: UUID): ResponseEntity<Book> {
        val book = repository.findById(id)
        if (book.isPresent) {
            return ResponseEntity<Book>(book.get(), HttpStatus.OK)
        } else {
            return ResponseEntity<Book>(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(params = ["isbn"])
    fun getByIsbn(@RequestParam isbn: String): ResponseEntity<Book> {
        val bookIsbn = repository.findByIsbn(isbn)
        if (bookIsbn != null) {
            return ResponseEntity.ok(bookIsbn)
        }
        return ResponseEntity<Book>(HttpStatus.NOT_FOUND)
    }

    @GetMapping(params = ["title"])
    fun getByTitle(@RequestParam title: String): ResponseEntity<Book> {
        val bookTitle = repository.findByTitle(title)
        if (bookTitle != null) {
            return ResponseEntity.ok(bookTitle)
        }
        return ResponseEntity<Book>(HttpStatus.NOT_FOUND)
    }

    @GetMapping(params = ["author"])
    fun getByAuthor(@RequestParam author: String): ResponseEntity<List<Book>> {
        val bookAuthor = repository.findByAuthor(author)
        if (bookAuthor != null) {
            return ResponseEntity.ok(bookAuthor)
        }
        return ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND)
    }
}
