package com.libraryapi.librarymanagement.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.libraryapi.librarymanagement.repository.LoanRepository
import com.libraryapi.librarymanagement.rest.dto.LoanDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
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
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() = loanRepository.deleteAll()

    @AfterEach
    fun tearDown() = loanRepository.deleteAll()

    @Test
    fun `should create loan`() {
//        val user: User = userRepository.save(builderUserDto().toEntity())
        val request: LoanDto = builderLoanDto()

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").exists()
                MockMvcResultMatchers.jsonPath("$.userId").value(request.userId)
                MockMvcResultMatchers.jsonPath("$.bookId").value(request.bookId)
                MockMvcResultMatchers.jsonPath("$.issuedDate").value(request.issuedDate)
                MockMvcResultMatchers.jsonPath("$.dueDate").value(request.dueDate)
                MockMvcResultMatchers.jsonPath("$.returnedDate").value(request.returnedDate)
            }
        }
    }
}

private fun builderLoanDto(
    userId: String? = "USER_b2c192bd-b2b2-4f4f-8fba-53ebfaa5fa01",
    bookId: String? = "BOOK_44953548-0c7e-42ea-b207-7983578ccafc",
    issuedDate: LocalDate? = null,
    dueDate: LocalDate? = null,
    returnedDate: LocalDate? = null
) = LoanDto(
    id = null,
    userId = userId,
    bookId = bookId,
    issuedDate = issuedDate,
    dueDate = dueDate,
    returnedDate = returnedDate
)
