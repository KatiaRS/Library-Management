package com.libraryapi.librarymanagement.controller

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.repository.BookRepository
import jakarta.persistence.Id
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/books")
class BookController(private val repository: BookRepository) {

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    fun create(@RequestBody book: Book): Book = repository.save(book)

    @GetMapping()
    fun getAll(): List<Book> = repository.findAll()

    @GetMapping("/{id}")
    fun searchBook(@PathVariable id: UUID): ResponseEntity<Book>{
       val book = repository.findById(id)
       if (book.isPresent){
           return ResponseEntity<Book>(book.get(),HttpStatus.OK)
       } else {
           return ResponseEntity<Book>(HttpStatus.NOT_FOUND)
       }
    }
}
