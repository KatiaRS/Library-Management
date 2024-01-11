package com.libraryapi.librarymanagement.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID

@Entity
@Table
data class Loan(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne()
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne()
    @JoinColumn(name = "copy_id")
    var copy: Copy,

    val issuedDate: LocalDate = LocalDate.now(),

    val dueDate: LocalDate = LocalDate.now().plusDays(15),

    val returnDate: LocalDate? = null

)
