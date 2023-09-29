package com.libraryapi.librarymanagement.controller

import com.libraryapi.librarymanagement.domain.Book
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun create(@RequestBody books: Book): Book {
        return books

    }
}