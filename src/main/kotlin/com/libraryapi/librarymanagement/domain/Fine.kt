package com.libraryapi.librarymanagement.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table
data class Fine(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "loan_id")
    var loan: Loan = Loan(),

    var paidDate: LocalDate? = null,

    var amount: BigDecimal = BigDecimal.ZERO

) {
    fun isPaid(): Boolean {
        return paidDate != null
    }
}
