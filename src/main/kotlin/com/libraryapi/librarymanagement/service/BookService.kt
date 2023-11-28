package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.domain.BookAlreadyExistsException
import com.libraryapi.librarymanagement.domain.BookNotFoundException
import com.libraryapi.librarymanagement.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BookService(private val repository: BookRepository) {

    fun create(book: Book): Book {
        if (repository.existsByIsbnOrTitle(book.isbn, book.title)) {
            throw BookAlreadyExistsException()
        }

        return repository.save(book)
    }

    fun getAll(): List<Book> = repository.findAll()

    fun getById(id: UUID): Book {
        return repository.findById(id).orElseThrow {
            throw BookNotFoundException()
        }
    }

    fun getByIsbn(isbn: String): Book {
        return repository.findByIsbn(isbn) ?: throw BookNotFoundException()
    }

    fun getByTitle(title: String): Book {
        return repository.findByTitle(title) ?: throw BookNotFoundException()
    }

    fun getByAuthor(author: String): List<Book> {
        val bookAuthor = repository.findByAuthor(author)
        if (bookAuthor.isNullOrEmpty()) {
            throw BookNotFoundException()
        }
        return repository.findByAuthor(author)
    }

    fun deleteById(id: UUID) {
        if (!repository.existsById(id)) {
            throw BookNotFoundException()
        }
        repository.deleteById(id)
    }

    fun updateById(id: UUID, updatedBook: Book): Book {
        val updateBook = repository.findById(id).orElseThrow {
            throw BookNotFoundException()
        }
        updateBook.title = updatedBook.title
        updateBook.author = updatedBook.author
        updateBook.isbn = updatedBook.isbn

        return repository.save(updateBook)
    }
}
