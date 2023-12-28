package com.libraryapi.librarymanagement.rest.dto

import com.libraryapi.librarymanagement.domain.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF

const val USER_PREFIX = "USER_"
data class UserDto(
    val id: String?,

    @field:NotBlank
    @field:Size(max = 30)
    val firstName: String?,

    @field:NotBlank
    @field:Size(max = 30)
    val lastName: String?,

    @field:NotBlank
    @field:CPF
    val document: String?
)

fun UserDto.toEntity(): User = User(
    id = null,
    firstName = this.firstName!!,
    lastName = this.lastName!!,
    document = this.document!!

)
fun User.toDto(): UserDto = UserDto(
    id = "$USER_PREFIX$id",
    firstName = this.firstName,
    lastName = this.lastName,
    document = this.document
)
