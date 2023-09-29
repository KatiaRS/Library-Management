package com.libraryapi.librarymanagement.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Book(
    @Id
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,

)

