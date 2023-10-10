package com.libraryapi.librarymanagement.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table
data class Book(
    @Id
    @GeneratedValue
    var id:UUID? = null,
    val title: String = "",
    val author: String = "",
    val isbn: String = ""
)



