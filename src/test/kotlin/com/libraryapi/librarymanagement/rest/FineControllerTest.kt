package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.domain.Copy
import com.libraryapi.librarymanagement.domain.Fine
import com.libraryapi.librarymanagement.domain.Loan
import com.libraryapi.librarymanagement.repository.BookRepository
import com.libraryapi.librarymanagement.repository.CopyRepository
import com.libraryapi.librarymanagement.repository.FineRepository
import com.libraryapi.librarymanagement.repository.LoanRepository
import com.libraryapi.librarymanagement.repository.UserRepository
import com.libraryapi.librarymanagement.rest.dto.FINE_PREFIX
import com.libraryapi.librarymanagement.rest.dto.LOAN_PREFIX
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
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

private const val URL = "/fines"

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class FineControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var fineRepository: FineRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var copyRepository: CopyRepository

    @Autowired
    private lateinit var loanRepository: LoanRepository

    @BeforeEach
    @AfterEach
    fun setup() {
        fineRepository.deleteAll()
        loanRepository.deleteAll()
        copyRepository.deleteAll()
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `should pay fine`() {
        val issueDate = LocalDate.now().minusDays(30)
        val dueDate = LocalDate.now().minusDays(15)
        val fine = createFine(issueDate, dueDate)

        mockMvc.put("$URL/$FINE_PREFIX${fine.id}/pay") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").value("${FINE_PREFIX}${fine.id}")
                MockMvcResultMatchers.jsonPath("$.loan").value("${LOAN_PREFIX}${fine.loan.id}")
                MockMvcResultMatchers.jsonPath("$.paidDate").value(fine.paidDate)
                MockMvcResultMatchers.jsonPath("$.amount").value(fine.amount)
                MockMvcResultMatchers.jsonPath("$.paid").value(fine.isPaid())
            }
        }
    }

    @Test
    fun `should not pay a fine with an invalid ID `() {
        val fineIdInvalid = "Teste"

        mockMvc.put("$URL/$fineIdInvalid/pay") {
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
    fun `should not pay a fine if the fine has already been paid`() {
        val issueDate = LocalDate.now().minusDays(30)
        val dueDate = LocalDate.now().minusDays(15)
        var fine = createFine(issueDate, dueDate)

        fine = fineRepository.save(fine.copy(paidDate = LocalDate.now()))

        mockMvc.put("$URL/$FINE_PREFIX${fine.id}/pay") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Fine already paid")
            }
        }
    }

    @Test
    fun `should find fine by id `() {
        val issueDate = LocalDate.now().minusDays(30)
        val dueDate = LocalDate.now().minusDays(15)
        val fine = createFine(issueDate, dueDate)

        mockMvc.get("$URL/$FINE_PREFIX${fine.id}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").value("${FINE_PREFIX}${fine.id}")
                MockMvcResultMatchers.jsonPath("$.loan").value("${LOAN_PREFIX}${fine.loan.id}")
                MockMvcResultMatchers.jsonPath("$.paidDate").value(fine.paidDate)
                MockMvcResultMatchers.jsonPath("$.amount").value(fine.amount)
                MockMvcResultMatchers.jsonPath("$.paid").value(fine.isPaid())
            }
        }
    }

    fun createFine(issueDate: LocalDate, dueDate: LocalDate): Fine {
        val user = userRepository.save(builderUserDto().toEntity())
        val book = bookRepository.save(builderBookDto().toEntity())
        val copy = copyRepository.save(Copy(book = book))

        val loan = loanRepository.save(
            Loan(
                user = user,
                copy = copy,
                issueDate = issueDate,
                dueDate = dueDate,
                returnDate = LocalDate.now()
            )
        )

        return fineRepository.save(
            Fine(
                loan = loan,
                amount = BigDecimal(50)
            )
        )
    }
}
