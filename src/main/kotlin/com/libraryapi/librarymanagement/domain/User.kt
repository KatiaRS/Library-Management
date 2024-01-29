package com.libraryapi.librarymanagement.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.validator.constraints.br.CPF
import java.util.UUID

@Entity(name = "users")
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, length = 30)
    var firstName: String = "",

    @Column(nullable = false, length = 30)
    var lastName: String = "",

    @Column(unique = true, nullable = false, length = 11)
    @field:CPF
    val document: String = "",

    @OneToMany(mappedBy = "user")
    var loans: MutableList<Loan> = mutableListOf()

)
