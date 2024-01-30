package com.libraryapi.librarymanagement.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import com.libraryapi.librarymanagement.domain.Checkout
import com.libraryapi.librarymanagement.service.CheckoutService
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.math.BigDecimal

data class CheckoutRequest(
    val items: List<CheckoutItem>,
    val flow: String = "PAYMENT_LINK"
)

data class CheckoutItem(
    val name: String,
    val quantity: Int,

    @JsonProperty("unit_amount") val unitAmount: BigDecimal
)

data class CheckoutResponse(
    val id: String,
    val links: List<CheckoutLink>
)

data class CheckoutLink(
    val ref: String,
    val href: String,
    val method: String
)

@FeignClient(name = "checkoutApiClient", url = "https://qa.api.pagseguro.com")
interface CheckoutApiClient {
    @PostMapping("/checkouts")
    fun createCheckout(
        @RequestHeader("Authorization") token: String,
        @RequestBody checkoutRequest: CheckoutRequest
    ): CheckoutResponse
}

@Service
class PagBankCheckoutService(
    private val checkoutApiClient: CheckoutApiClient
) : CheckoutService {
    override fun createCheckout(description: String, value: BigDecimal): Checkout {
        val checkoutResponse = checkoutApiClient.createCheckout(
            token = "Bearer F856E9B3B86D4A79AC9A1D1290CC8B41",
            checkoutRequest = CheckoutRequest(
                items = listOf(
                    CheckoutItem(description, 1, value.multiply(BigDecimal(100)))
                )
            )
        )
        val code = checkoutResponse.id
        val link = checkoutResponse.links.first { it.ref.uppercase() == "PAY" }

        return Checkout(code, link = link.href)
    }
}
