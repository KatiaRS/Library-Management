package com.libraryapi.librarymanagement.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.libraryapi.librarymanagement.domain.Book
import com.libraryapi.librarymanagement.repository.BookRepository
import com.libraryapi.librarymanagement.repository.CopyRepository
import com.libraryapi.librarymanagement.rest.dto.BOOK_PREFIX
import com.libraryapi.librarymanagement.rest.dto.BookDto
import com.libraryapi.librarymanagement.rest.dto.toEntity
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

private const val URL = "/books"

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var copyRepository: CopyRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        copyRepository.deleteAll()
        bookRepository.deleteAll() // limpa o banco de dados cada vez q for rodar
    }

    @AfterEach
    fun tearDown() {
        copyRepository.deleteAll()
        bookRepository.deleteAll() // limpa o banco de dados quando finaliza o teste
    }

    @Test
    fun `should create book`() {
        // dado quando eu crio um book
        val request: BookDto = builderBookDto()

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.id").exists()
                jsonPath("$.title").value(request.title)
                jsonPath("$.author").value(request.author)
                jsonPath("$.isbn").value(request.isbn)
            }
        }
    }

    @Test
    fun `should find a list of books`() {
        // Dado dois livros existentes
        val book1: Book = bookRepository.save(
            Book(
                id = null,
                title = "As princesas",
                author = "Luan Santana",
                isbn = "12345678910"
            )
        )

        val book2: Book = bookRepository.save(
            Book(
                id = null,
                title = "O Sol",
                author = "Karen Andrade",
                isbn = "9090976544383"
            )
        )
        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    jsonPath(
                        "$[*].id",
                        Matchers.containsInAnyOrder("${BOOK_PREFIX}${book1.id}", "${BOOK_PREFIX}${book2.id}")

                    )
                    jsonPath("$[*].title", Matchers.containsInAnyOrder(book1.title, book2.title))
                    jsonPath("$[*].author", Matchers.containsInAnyOrder(book1.author, book2.author))
                    jsonPath("$[*].isbn", Matchers.containsInAnyOrder(book1.isbn, book2.isbn))
                }
            }
    }

    @Test
    fun `should create a copy of the book`() {
        // Dado um livro existente
        val book: Book = bookRepository.save(builderBookDto().toEntity())

        // Quando uma cópia do livro é criada
        mockMvc.post("$URL/$BOOK_PREFIX${book.id}/copy").andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `should not add a book copy with invalid ID and return 400`() {
        val invalidId: String = "Teste"
        val copyBook: BookDto = builderBookUpdateDto()

        // Quando eu faço um get
        mockMvc.post("$URL/$invalidId/copy") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(copyBook)
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.title").value("Bad Request")
                    jsonPath("$.status").value(400)
                    jsonPath("$.detail").value("Invalid Id")
                }
            }
    }

    @Test
    fun `should not create book with null title`() {
        val request: BookDto = builderBookDto(title = null)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create book with empty title`() {
        val request: BookDto = builderBookDto(title = "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create book with null author`() {
        val request: BookDto = builderBookDto(author = null)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create book with empty author`() {
        val request: BookDto = builderBookDto(author = "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create book with null isbn`() {
        val request: BookDto = builderBookDto(isbn = null)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create book with empty isbn`() {
        val request: BookDto = builderBookDto(isbn = "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should return status 400 if trying to create a book with a repeated ISBN`() {
        val request: BookDto = builderBookDto()
        bookRepository.save(Book(null, "O Poder do Não", "José Augusto", request.isbn!!))

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("This book already exists")
            }
        }
    }

    @Test
    fun `should return status 400 if trying to create a book with a repeated title`() {
        val request: BookDto = builderBookDto()
        bookRepository.save(Book(null, request.title!!, "José Augusto", "1090923448469"))

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("This book already exists")
            }
        }
    }

    @Test
    fun `should not create a book with a title longer than 255 characters `() {
        val titleLong = "a".repeat(256)
        val request: BookDto = builderBookDto(title = titleLong)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create a book with a author longer than 255 characters `() {
        val titleLong = "a".repeat(256)
        val request: BookDto = builderBookDto(author = titleLong)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                jsonPath("$.title").value("Bad Request")
                jsonPath("$.status").value(400)
                jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should find book by id`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(Book(null, "A Estagiária", "Pag Bank", "0000000000000"))

        // Quando eu faço um get
        mockMvc.get("$URL/$BOOK_PREFIX${book.id}") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${book.id}")
                    jsonPath("$.author").value(book.author)
                    jsonPath("$.isbn").value(book.isbn)
                    jsonPath("$.title").value(book.title)
                }
            }
    }

    @Test
    fun `should not find book with invalid id and return 400`() {
        val invalidId: String = "Teste"

        // Quando eu faço um get
        mockMvc.get("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.title").value("Bad Request")
                    jsonPath("$.status").value(400)
                    jsonPath("$.detail").value("Invalid Id")
                }
            }
    }

    @Test
    fun `should find book by Isbn`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(Book(null, "A Estagiária", "Pag Bank", "0000000000000"))

        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
            param("isbn", book.isbn)
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${book.id}")
                    jsonPath("$.author").value(book.author)
                    jsonPath("$.isbn").value(book.isbn)
                    jsonPath("$.title").value(book.title)
                }
            }
    }

    @Test
    fun `should find book by title`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(Book(null, "A Estagiária", "Pag Bank", "0000000000000"))

        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
            param("title", book.title)
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${book.id}")
                    jsonPath("$.author").value(book.author)
                    jsonPath("$.isbn").value(book.isbn)
                    jsonPath("$.title").value(book.title)
                }
            }
    }

    @Test
    fun `should find book by author`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(Book(null, "A Estagiária", "Pag Bank", "0000000000000"))

        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
            param("author", book.author)
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${book.id}")
                    jsonPath("$.author").value(book.author)
                    jsonPath("$.isbn").value(book.isbn)
                    jsonPath("$.title").value(book.title)
                }
            }
    }

    @Test
    fun `should not find a book with an empty author`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(Book(null, "A Estagiária", "Pag Bank", "0000000000000"))

        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
            param("author", "")
        }
            // Então esperamos um status ok
            .andExpect {
                status { isNotFound() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${book.id}")
                    jsonPath("$.author").value(book.author)
                    jsonPath("$.isbn").value(book.isbn)
                    jsonPath("$.title").value(book.title)
                }
            }
    }

    @Test
    fun `should not find a book with an null author`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(Book(null, "A Estagiária", "Pag Bank", "0000000000000"))

        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
            param("author", null.toString())
        }
            // Então esperamos um status ok
            .andExpect {
                status { isNotFound() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${book.id}")
                    jsonPath("$.author").value(book.author)
                    jsonPath("$.isbn").value(book.isbn)
                    jsonPath("$.title").value(book.title)
                }
            }
    }

    @Test
    fun `should update Book`() {
        // Dado um livro salvo no banco
        val book: Book = bookRepository.save(builderBookDto().toEntity())
        val bookUpdateDto: BookDto = builderBookUpdateDto()

        // Quando eu faço um get
        mockMvc.put("$URL/$BOOK_PREFIX${book.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookUpdateDto)
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id").value("$BOOK_PREFIX${bookUpdateDto.id}")
                    jsonPath("$.author").value(bookUpdateDto.author)
                    jsonPath("$.isbn").value(bookUpdateDto.isbn)
                    jsonPath("$.title").value(bookUpdateDto.title)
                }
            }
    }

    @Test
    fun `should not update a book with a non-existent id and return 404`() {
        val invalidId: UUID = UUID.randomUUID()
        val bookUpdateDto: BookDto = builderBookUpdateDto()

        // Quando eu faço um put
        mockMvc.put("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookUpdateDto)
        }
            // Então esperamos um status
            .andExpect {
                status { isNotFound() }
                content {
                    jsonPath("$.title").value("Not Found")
                    jsonPath("$.status").value(404)
                    jsonPath("$.detail").value("Book not found")
                }
            }
    }

    @Test
    fun `should not update a book with a invalid id and return 400`() {
        val invalidId: String = "Teste"
        val bookUpdateDto: BookDto = builderBookUpdateDto()

        // Quando eu faço um get
        mockMvc.put("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookUpdateDto)
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.title").value("Bad Request")
                    jsonPath("$.status").value(400)
                    jsonPath("$.detail").value("Invalid Id")
                }
            }
    }

    @Test
    fun `should delete Book by id`() {
        val book: Book = bookRepository.save(builderBookDto().toEntity())

        mockMvc.delete("$URL/$BOOK_PREFIX${book.id}") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    fun `should not delete Book with a non-existent id and return 404 status`() {
        val invalidId: UUID = UUID.randomUUID()

        mockMvc.delete("$URL/$BOOK_PREFIX$invalidId") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isNotFound() }
                content {
                    jsonPath("$.title").value("Not Found")
                    jsonPath("$.status").value(404)
                    jsonPath("$.detail").value("Book not found")
                }
            }
    }

    @Test
    fun `should not delete a book with a invalid id and return 400`() {
        val invalidId: String = "Teste"

        // Quando eu faço um get
        mockMvc.delete("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.title").value("Bad Request")
                    jsonPath("$.status").value(400)
                    jsonPath("$.detail").value("Invalid Id")
                }
            }
    }
}

fun builderBookDto(
    id: String? = null,
    title: String? = "A Lua",
    author: String? = "Katia Santana",
    isbn: String? = "123345678962"
) = BookDto(
    id = id,
    title = title,
    isbn = isbn,
    author = author
)

fun builderBookUpdateDto(
    title: String? = "Legião Urbana",
    author: String? = "Renato Russo",
    isbn: String? = "123345678963"
): BookDto = BookDto(
    id = null,
    title = title,
    author = author,
    isbn = isbn
)
