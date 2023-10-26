package com.libraryapi.librarymanagement.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*
import kotlin.math.min

@Entity
@Table
data class Book(
    @Id
    @GeneratedValue
    var id: UUID? = null,

    @Column(unique = true, length = 255, nullable = false)
    val title: String = "",

    @Column(length = 255, nullable = false)
    val author: String = "",

    @Column(unique = true, nullable = false)
    @Size(min = 10, max = 13)
    val isbn: String = ""
)



