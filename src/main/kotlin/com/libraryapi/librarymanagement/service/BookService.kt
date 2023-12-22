package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.exception.BookAlreadyExistsException
import com.libraryapi.librarymanagement.exception.BookNotFoundException
import com.libraryapi.librarymanagement.repository.BookRepository
import com.libraryapi.librarymanagement.repository.CopyRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val copyRepository: CopyRepository
) {

    fun create(book: Book): Book {
        if (bookRepository.existsByIsbnOrTitle(book.isbn, book.title)) {
            throw BookAlreadyExistsException()
        }
        return bookRepository.save(book).also {
            copyRepository.save(Copy(book = it))
        }
    }

    fun addCopy(id: UUID): Copy {
        return copyRepository.save(Copy(book = getById(id)))
    }

    fun getAll(): List<Book> {
        return bookRepository.findAll()
    }

    fun getById(id: UUID): Book {
        return bookRepository.findById(id).orElseThrow {
            throw BookNotFoundException()
        }
    }

    fun getByIsbn(isbn: String): Book {
        return bookRepository.findByIsbn(isbn) ?: throw BookNotFoundException()
    }

    fun getByTitle(title: String): Book {
        return bookRepository.findByTitle(title) ?: throw BookNotFoundException()
    }

    fun getByAuthor(author: String): List<Book> {
        val bookAuthor = bookRepository.findByAuthor(author)
        if (bookAuthor.isEmpty()) {
            throw BookNotFoundException()
        }
        return bookAuthor
    }

    fun deleteById(id: UUID) {
        bookRepository.delete(getById(id))
    }

    fun updateById(id: UUID, updatedBook: Book): Book {
        val updateBook = getById(id)
        updateBook.title = updatedBook.title
        updateBook.author = updatedBook.author
        updateBook.isbn = updatedBook.isbn

        return bookRepository.save(updateBook)
    }
}
