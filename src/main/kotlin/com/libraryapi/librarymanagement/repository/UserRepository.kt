package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByDocument(document: String): User

    fun existsByDocument(document: String): Boolean
}
