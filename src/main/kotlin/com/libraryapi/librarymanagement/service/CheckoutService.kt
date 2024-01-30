package com.libraryapi.librarymanagement.service

import com.libraryapi.librarymanagement.domain.Checkout
import java.math.BigDecimal

interface CheckoutService {

    fun createCheckout(description: String, value: BigDecimal): Checkout
}
