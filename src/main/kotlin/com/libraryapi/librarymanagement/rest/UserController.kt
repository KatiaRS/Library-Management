package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.exception.ConversionIdException
import com.libraryapi.librarymanagement.rest.dto.USER_PREFIX
import com.libraryapi.librarymanagement.rest.dto.UserDto
import com.libraryapi.librarymanagement.rest.dto.toDto
import com.libraryapi.librarymanagement.rest.dto.toEntity
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
        return userService.create(userDto.toEntity()).toDto()
    }

    @GetMapping
    fun getAll(): List<UserDto> = userService.getAll().map { it.toDto() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): UserDto {
        return userService.getById(convertId(id)).toDto()
    }

    @GetMapping(params = ["document"])
    fun getByDocument(@RequestParam document: String): UserDto {
        return userService.getByDocument(document)!!.toDto()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String) = userService.deleteById(convertId(id))

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: String, @RequestBody @Valid userDto: UserDto): UserDto {
        return userService.updateById(convertId(id), userDto.toEntity()).toDto()
    }

    private fun convertId(id: String): UUID {
        val noPrefixUser = id.removePrefix(USER_PREFIX)
        try {
            return UUID.fromString(noPrefixUser)
        } catch (e: Exception) {
            throw ConversionIdException()
        }
    }
}
