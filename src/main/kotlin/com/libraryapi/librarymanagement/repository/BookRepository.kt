package com.libraryapi.librarymanagement.repository

import com.libraryapi.librarymanagement.domain.Book
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookRepository: JpaRepository<Book, UUID>
