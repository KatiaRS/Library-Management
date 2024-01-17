package com.libraryapi.librarymanagement.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.repository.BookRepository
import com.libraryapi.librarymanagement.repository.CopyRepository
import com.libraryapi.librarymanagement.repository.LoanRepository
import com.libraryapi.librarymanagement.repository.UserRepository
import com.libraryapi.librarymanagement.rest.dto.BOOK_PREFIX
import com.libraryapi.librarymanagement.rest.dto.LOAN_PREFIX
import com.libraryapi.librarymanagement.rest.dto.LoanDto
import com.libraryapi.librarymanagement.rest.dto.USER_PREFIX
import com.libraryapi.librarymanagement.rest.dto.toEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

private const val URL = "/loans"

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class LoanControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var loanRepository: LoanRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var copyRepository: CopyRepository

    @BeforeEach
    @AfterEach
    fun setup() {
        loanRepository.deleteAll()
        copyRepository.deleteAll()
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `should create loan`() {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        copyRepository.save(Copy(book = book))
        val request = builderLoanDto("$USER_PREFIX${user.id}", "$BOOK_PREFIX${book.id}")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").exists()
                MockMvcResultMatchers.jsonPath("$.userId").value(user.id)
                MockMvcResultMatchers.jsonPath("$.bookId").value(book.id)
                MockMvcResultMatchers.jsonPath("$.issuedDate").value(LocalDate.now())
                MockMvcResultMatchers.jsonPath("$.dueDate").value(LocalDate.now().plusDays(15))
                MockMvcResultMatchers.jsonPath("$.returnDate").doesNotExist()
            }
        }
    }

    @Test // testar conforme a regra de negocio
    fun `should not create loan  for unavailable book copy`() {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))
        loanRepository.save(Loan(user = user, copy = copy))

        val request = builderLoanDto("$USER_PREFIX${user.id}", "$BOOK_PREFIX${book.id}")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("No books available")
            }
        }
    }

    @Test
    fun `should not create loan when user already has 5 active loans`() {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        repeat(5) {
            val copy = copyRepository.save(Copy(book = book))
            loanRepository.save(Loan(user = user, copy = copy))
        }
        val request = builderLoanDto("$USER_PREFIX${user.id}", "$BOOK_PREFIX${book.id}")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Loan limit reached by user")
            }
        }
    }

    @Test
    fun `should not create loan with userId empty`() {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))
        loanRepository.save(Loan(user = user, copy = copy))

        val request = builderLoanDto("", "$BOOK_PREFIX${copy.id}")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("User not found")
            }
        }
    }

    @Test
    fun `should not create loan with bookId empty`() {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))
        loanRepository.save(Loan(user = user, copy = copy))

        val request = builderLoanDto("$USER_PREFIX${user.id}", "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("d not found")
            }
        }
    }

    @Test
    fun `should find loan by id `() {
        // dado um loan salvo
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))
        val loan = loanRepository.save(Loan(user = user, copy = copy))

        // quando faço um get pelo id do loan
        mockMvc.get("$URL/$LOAN_PREFIX${loan.id}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").exists()
                MockMvcResultMatchers.jsonPath("$.userId").value(user.id)
                MockMvcResultMatchers.jsonPath("$.bookId").value(book.id)
                MockMvcResultMatchers.jsonPath("$.issuedDate").value(LocalDate.now())
                MockMvcResultMatchers.jsonPath("$.dueDate").value(LocalDate.now().plusDays(15))
                MockMvcResultMatchers.jsonPath("$.returnDate").doesNotExist()
            }
        }
    }

    @Test
    fun `should not find loan with invalid id and return 400 `() {
        // dado um id inválido
        val invalidIdLoan = "Teste"

        mockMvc.get("$URL/$invalidIdLoan") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                    MockMvcResultMatchers.jsonPath("$.status").value(400)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Loan not found")
                }
            }
    }

    @Test
    fun `should return the book`() {
        // dado um livro emprestado
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))
        val loan = loanRepository.save(Loan(user = user, copy = copy))

        // quando eu faço um put, deve me retonrar OK
        mockMvc.put("$URL/$LOAN_PREFIX${loan.id}/devolution") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").exists()
                MockMvcResultMatchers.jsonPath("$.userId").value(user.id)
                MockMvcResultMatchers.jsonPath("$.bookId").value(book.id)
                MockMvcResultMatchers.jsonPath("$.issuedDate").value(LocalDate.now())
                MockMvcResultMatchers.jsonPath("$.dueDate").value(LocalDate.now().plusDays(15))
                MockMvcResultMatchers.jsonPath("$.returnDate").value(LocalDate.now())
            }
        }
    }

    @Test
    fun `should not return loan with invalid id and return 404`() {
        val invalidIdLoan = "Teste"

        mockMvc.put("$URL/$invalidIdLoan/devolution") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Invalid Id")
            }
        }
    }

    @Test
    fun `should not return book because it is already returned`() {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))
        val loan = loanRepository.save(Loan(user = user, copy = copy))

        loan.returnDate = LocalDate.now()
        loanRepository.save(loan)

        mockMvc.put("$URL/${loan.id}/devolution") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Invalid Id")
            }
        }
    }

    @Test
    fun `should return 400 if the user does not exist`() {
        val userInvalid = builderLoanDto()

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("User not found")
            }
        }
    }

    @Test
    fun `should return 400 if the book does not exist`() {
        val bookInvalid = builderLoanDto()

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("User not found")
            }
        }
    }
}

private fun builderLoanDto(

    userId: String? = "USER_b2c192bd-b2b2-4f4f-8fba-53ebfaa5fa01",
    bookId: String? = "BOOK_44953548-0c7e-42ea-b207-7983578ccafc"

) = LoanDto(
    userId = userId,
    bookId = bookId
)
