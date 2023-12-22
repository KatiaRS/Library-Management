package com.libraryapi.librarymanagement.rest.dto

import com.libraryapi.librarymanagement.domain.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF

const val USER_PREFIX = "USER_"
data class UserDto(
    val idUser: String?,

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

fun UserDto.toUser(): User = User(
    idUser = null,
    firstName = this.firstName!!,
    lastName = this.lastName!!,
    document = this.document!!

)
fun User.toUserDto(): UserDto = UserDto(
    idUser = "$USER_PREFIX$idUser",
    firstName = this.firstName,
    lastName = this.lastName,
    document = this.document
)
