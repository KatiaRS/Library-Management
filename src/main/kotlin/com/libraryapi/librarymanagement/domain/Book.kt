package com.libraryapi.librarymanagement.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Length
import java.util.*
import kotlin.math.min

@Entity
@Table
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, length = 255, nullable = false)
    var title: String = "",

    @Column(length = 255, nullable = false)
    var author: String = "",

    @Column(unique = true, nullable = false)
    @field:Length(min = 10, max = 13)
    var isbn: String = ""
)


