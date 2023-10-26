package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.repository.BookRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


@SpringBootTest
@AutoConfigureMockMvc

class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun `should create book`() {
        val postPath = "/books"
        val body = """{
        "title": "O Chamado ",
        "author": "Katia Santana",
        "isbn": "12345678910"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `should not create book with null title`() {
        val postPath = "/books"
        val body = """{
        "title": null,
        "author": "Katia Santana",
        "isbn": "1234"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should not create book with empty title`() {
        val postPath = "/books"
        val body = """{
        "title": "",
        "author": "Katia Santana",
        "isbn": "0606"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should not create book with null author`() {
        val postPath = "/books"
        val body = """{
        "title": "A Lua",
        "author": null,
        "isbn": "1234"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should not create book with empty author`() {
        val postPath = "/books"
        val body = """{
        "title": "A Lua",
        "author": "",
        "isbn": "1234"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should not create book with null isbn`() {
        val postPath = "/books"
        val body = """{
        "title": "A Lua",
        "author": "Katia Santana",
        "isbn": null
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should not create book with empty isbn`() {
        val postPath = "/books"
        val body = """{
        "title": "A Lua",
        "author": "Katia Santana",
        "isbn": ""
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should not create book with repeated isbn or title`() {
        val postPath = "/books"
        val body = """{
        "title": "A Lua",
        "author": "Pedro Miguel",
        "isbn": "1234"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isOk() }
        }
        val bookIsbnDuplicate = """{
        "title": "As estrelas",
        "author": "Katia Santana",
        "isbn": "1234"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = bookIsbnDuplicate
        }.andExpect {
            status { isBadRequest() }
        }

        val bookTitleDuplicate = """{
        "title": "A Lua",
        "author": "Luan Santana",
        "isbn": "0606"
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = bookTitleDuplicate
        }.andExpect {
            status { isBadRequest() }
        }

    }

    @Test
    fun `You should not create a book with a title longer than 255 characters `() {
        val postPath = "/books"
        val title = "A".repeat(255)
        val body = """{
        "title": "$title",
        "author": "Katia Santana",
        "isbn": ""
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `You should not create a book with a author longer than 255 characters `() {
        val postPath = "/books"
        val author = "A".repeat(255)
        val body = """{
        "title": "A lua",
        "author": "$author",
        "isbn": ""
        }"""
        mockMvc.post(postPath) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should get book the id`() {
        val book = Book(null, "As estrelas", "Katia Santana", "123456")
        val saveBook = bookRepository.save(book)

        mockMvc.get("/books/${saveBook.id}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `you should look for the ISBN`() {
        val book = Book(null, "O Chamado", "Katia Santana", "12345")
        val saveBook = bookRepository.save(book)

        mockMvc.get("/books?isbn=${saveBook.isbn}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `you should look for the title`() {
        val book = Book(null, "O Sol", "Katia Santana", "010102")
        val saveBook = bookRepository.save(book)

        mockMvc.get("/books?title=${saveBook.title}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `you should look for the author`() {
        val book = Book(null, "O Poder", "Katia Santana", "060617")
        val saveBook = bookRepository.save(book)

        mockMvc.get("/books?author=${saveBook.author}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

}