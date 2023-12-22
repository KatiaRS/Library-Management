package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.User
import com.libraryapi.librarymanagement.exception.BookNotFoundException
import com.libraryapi.librarymanagement.exception.UserAlreadyExistsException
import com.libraryapi.librarymanagement.exception.UserNotFoundException
import com.libraryapi.librarymanagement.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun create(user: User): User {
        if (userExist(user)) {
            throw UserAlreadyExistsException()
        }
        return userRepository.save(user)
    }

    fun getAll(): List<User> = userRepository.findAll()

    fun getById(id: UUID): User {
        return userRepository.findById(id).orElseThrow {
            throw BookNotFoundException()
        }
    }

    fun getByDocument(document: String): User? {
        return userRepository.findByDocument(document) ?: throw UserNotFoundException()
    }

    fun deleteById(idUser: UUID) {
        userRepository.delete(getById(idUser))
    }

    fun updateById(idUser: UUID, updatedUser: User): User {
        val updateUser = getById(idUser)
        if (updatedUser.document != updateUser.document) {
            throw UserAlreadyExistsException()
        }
        updateUser.firstName = updatedUser.firstName
        updatedUser.lastName = updatedUser.lastName

        return userRepository.save(updateUser)
    }

    private fun userExist(user: User) = userRepository.existsByDocument(user.document)
}
