package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.exception.ConversionIdException
import com.libraryapi.librarymanagement.rest.dto.USER_PREFIX
import com.libraryapi.librarymanagement.rest.dto.UserDto
import com.libraryapi.librarymanagement.rest.dto.toUser
import com.libraryapi.librarymanagement.rest.dto.toUserDto
import com.libraryapi.librarymanagement.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun create(
        @RequestBody @Valid
        userDto: UserDto
    ): UserDto {
        return userService.create(userDto.toUser()).toUserDto()
    }

    @GetMapping
    fun getAll(): List<UserDto> = userService.getAll().map { it.toUserDto() }

    @GetMapping("/{idUser}")
    fun getById(@PathVariable idUser: String): UserDto {
        return userService.getById(convertIdUser(idUser)).toUserDto()
    }

    @GetMapping(params = ["document"])
    fun getByDocument(@RequestParam document: String): UserDto {
        return userService.getByDocument(document)!!.toUserDto()
    }

    @DeleteMapping("/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable idUser: String) = userService.deleteById(convertIdUser(idUser))

    @PutMapping("/{idUser}")
    fun updateById(@PathVariable idUser: String, @RequestBody @Valid userDto: UserDto): UserDto {
        return userService.updateById(convertIdUser(idUser), userDto.toUser()).toUserDto()
    }

    private fun convertIdUser(idUser: String): UUID {
        val noPrefixUser = idUser.removePrefix(USER_PREFIX)
        try {
            return UUID.fromString(noPrefixUser)
        } catch (e: Exception) {
            throw ConversionIdException()
        }
    }
}
