package com.libraryapi.librarymanagement.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.libraryapi.librarymanagement.domain.User
import com.libraryapi.librarymanagement.repository.UserRepository
import com.libraryapi.librarymanagement.rest.dto.USER_PREFIX
import com.libraryapi.librarymanagement.rest.dto.UserDto
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

private const val URL = "/users"

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    @AfterEach
    fun setup() = userRepository.deleteAll()

    @Test
    fun `should create user`() {
        val request: UserDto = builderUserDto()

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                MockMvcResultMatchers.jsonPath("$.id").exists()
                MockMvcResultMatchers.jsonPath("$.firstname").value(request.firstName)
                MockMvcResultMatchers.jsonPath("$.lastname").value(request.lastName)
                MockMvcResultMatchers.jsonPath("$.document").value(request.document)
            }
        }
    }

    @Test
    fun `should not create user and return 409`() {
        val user: User = userRepository.save(builderUserDto().toEntity())
        val user2: UserDto = builderUserDto("Beatriz", "Andrade", user.document)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(user2)
        }.andExpect {
            status { isConflict() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Conflict")
                MockMvcResultMatchers.jsonPath("$.status").value(409)
                MockMvcResultMatchers.jsonPath("$.detail").value("This user already exists")
            }
        }
    }

    @Test
    fun `should not create user with null firstname`() {
        val request: UserDto = builderUserDto(firstName = null)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Failed to read request")
            }
        }
    }

    @Test
    fun `should not create user with null lastname`() {
        val request: UserDto = builderUserDto(lastName = null)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Failed to read request")
            }
        }
    }

    @Test
    fun `should not create user with null document`() {
        val request: UserDto = builderUserDto(document = null)

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Failed to read request")
            }
        }
    }

    @Test
    fun `should not create user with empty firstname`() {
        val request: UserDto = builderUserDto(firstName = "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create user with empty lastname`() {
        val request: UserDto = builderUserDto(lastName = "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should not create user with empty document`() {
        val request: UserDto = builderUserDto(document = "")

        mockMvc.post(URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Invalid request content.")
            }
        }
    }

    @Test
    fun `should return all users`() {
        // dado quando eu crio uma lista de usuários
        // E salvo no banco

        val user1: User = userRepository.save(
            User(
                id = null,
                firstName = "Katia",
                lastName = "Santana",
                document = "52701032067"
            )
        )
        val user2: User = userRepository.save(
            User(
                id = null,
                firstName = "Pedro",
                lastName = "Souza",
                document = "23357953099"
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
                        Matchers.containsInAnyOrder("$USER_PREFIX${user1.id}", "$USER_PREFIX${user2.id}")

                    )
                    jsonPath("$[*].firstName", Matchers.containsInAnyOrder(user1.firstName, user2.firstName))
                    jsonPath("$[*].lastName", Matchers.containsInAnyOrder(user1.lastName, user2.lastName))
                    jsonPath("$[*].document", Matchers.containsInAnyOrder(user1.document, user2.document))
                }
            }
    }

    @Test
    fun `should find user by id`() {
        // Dado um usuário salvo no banco
        val user: User = userRepository.save(User(null, "Karen Beatriz", "Santana", "06472583072"))

        // Quando eu faço um get
        mockMvc.get("$URL/$USER_PREFIX${user.id}") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    MockMvcResultMatchers.jsonPath("$.idUser").value("$USER_PREFIX${user.id}")
                    MockMvcResultMatchers.jsonPath("$.firstname").value(user.firstName)
                    MockMvcResultMatchers.jsonPath("$.lastname").value(user.lastName)
                    MockMvcResultMatchers.jsonPath("$.document").value(user.document)
                }
            }
    }

    @Test
    fun `should not find user with invalid id and return 400`() {
        val invalidId: String = "Teste"

        // Quando eu faço um get
        mockMvc.get("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                    MockMvcResultMatchers.jsonPath("$.status").value(400)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Invalid Id")
                }
            }
    }

    @Test
    fun `should find user by document`() {
        // Dado um usuário salvo no banco
        val user: User = userRepository.save(User(null, "Karen", "Santana", "69638232005"))

        // Quando eu faço um get
        mockMvc.get(URL) {
            contentType = MediaType.APPLICATION_JSON
            param("document", user.document)
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    MockMvcResultMatchers.jsonPath("$.idUser").value("$USER_PREFIX${user.id}")
                    MockMvcResultMatchers.jsonPath("$.firstname").value(user.firstName)
                    MockMvcResultMatchers.jsonPath("$.lastname").value(user.lastName)
                    MockMvcResultMatchers.jsonPath("$.document").value(user.document)
                }
            }
    }

    @Test
    fun `should not find user with invalid document and return 400`() {
        val invalidDocument: String = "Teste"

        // Quando eu faço um get
        mockMvc.get("$URL/$invalidDocument") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                    MockMvcResultMatchers.jsonPath("$.status").value(400)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Invalid Id")
                }
            }
    }

    @Test
    fun `should update user`() {
        // Dado um usuário salvo no banco
        val user: User = userRepository.save(builderUserDto().toEntity())
        val userUpdateDto: UserDto = builderUserUpdateDto()

        // Quando eu faço um put
        mockMvc.put("$URL/$USER_PREFIX${user.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userUpdateDto)
        }
            // Então esperamos um status ok
            .andExpect {
                status { isOk() }
                content {
                    MockMvcResultMatchers.jsonPath("$.id").value("$USER_PREFIX${user.id}")
                    MockMvcResultMatchers.jsonPath("$.firstname").value(user.firstName)
                    MockMvcResultMatchers.jsonPath("$.lastname").value(user.lastName)
                    MockMvcResultMatchers.jsonPath("$.document").value(user.document)
                }
            }
    }

    @Test
    fun `should not update a user with a non-existent id and return 404`() {
        val invalidId: UUID = UUID.randomUUID()
        val userUpdateDto: UserDto = builderUserUpdateDto()

        // Quando eu faço um get
        mockMvc.put("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userUpdateDto)
        }
            // Então esperamos um status
            .andExpect {
                status { isNotFound() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Not Found")
                    MockMvcResultMatchers.jsonPath("$.status").value(404)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Book not found")
                }
            }
    }

    @Test
    fun `should not update a user with a invalid id and return 400`() {
        val invalidId: String = "Teste"
        val userUpdateDto: UserDto = builderUserUpdateDto()

        // Quando eu faço um get
        mockMvc.put("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userUpdateDto)
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                    MockMvcResultMatchers.jsonPath("$.status").value(400)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Invalid Id")
                }
            }
    }

    @Test
    fun `should not update the user with changed document and return 400`() {
        val user: User = userRepository.save(builderUserDto().toEntity())
        val userUpdateDto: UserDto = builderUserUpdateDto(document = "39007924028")

        mockMvc.put("$URL/$USER_PREFIX${user.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userUpdateDto)
        }.andExpect {
            status { isBadRequest() }
            content {
                MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                MockMvcResultMatchers.jsonPath("$.status").value(400)
                MockMvcResultMatchers.jsonPath("$.detail").value("Document cannot be changed")
            }
        }
    }

    @Test
    fun `should delete User by id`() {
        val user: User = userRepository.save(builderUserDto().toEntity())

        mockMvc.delete("$URL/$USER_PREFIX${user.id}") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isNoContent() }
            }
    }

    @Test
    fun `should not delete User with a non-existent id and return 404 status`() {
        val invalidId: UUID = UUID.randomUUID()

        mockMvc.delete("$URL/$USER_PREFIX$invalidId") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isNotFound() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Not Found")
                    MockMvcResultMatchers.jsonPath("$.status").value(404)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Book not found")
                }
            }
    }

    @Test
    fun `should not delete a user with a invalid id and return 400`() {
        val invalidId: String = "Teste"

        // Quando eu faço um get
        mockMvc.delete("$URL/$invalidId") {
            contentType = MediaType.APPLICATION_JSON
        }
            // Então esperamos um status
            .andExpect {
                status { isBadRequest() }
                content {
                    MockMvcResultMatchers.jsonPath("$.title").value("Bad Request")
                    MockMvcResultMatchers.jsonPath("$.status").value(400)
                    MockMvcResultMatchers.jsonPath("$.detail").value("Invalid Id")
                }
            }
    }
}
fun builderUserDto(
    id: String? = null,
    firstName: String? = "Katia",
    lastName: String? = "Santana",
    document: String? = "52701032067"
) = UserDto(
    id = id,
    firstName = firstName,
    lastName = lastName,
    document = document
)
fun builderUserUpdateDto(
    firstName: String? = "Katia",
    lastName: String? = "Santana",
    document: String? = "52701032067"
): UserDto = UserDto(
    id = null,
    firstName = firstName,
    lastName = lastName,
    document = document
)
