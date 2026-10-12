package application.book.domain.models

import application.book.handlers.InvalidISBN
/**
 * Value Object: ISBN
 * Representa um ISBN válido (10 ou 13 dígitos)
 * Validação de check-digit garantida no construtor
 */

data class Isbn(val value: String) {

    init {
        val normalized = value
            .replace("-", "")
            .replace(" ", "")
            .uppercase()

        require(isValidISBN(normalized)) {
            throw InvalidISBN(isbn = this)
        }
    }

    companion object {
        /**
         * Factory method para criar ISBN com validação
         * @throws InvalidIsbn se ISBN inválido
         */
        fun of(value: String): Isbn {
            return Isbn(value)  // Valida no init
        }

        private fun isValidISBN(isbn: String): Boolean {
            return when (isbn.length) {
                10 -> isValidISBN10(isbn)
                13 -> isValidISBN13(isbn)
                else -> false
            }
        }

        private fun isValidISBN10(isbn: String): Boolean {
            if (!isbn.matches(Regex("""^\d{9}[\dX]$""")))
                return false

            val sum = isbn.mapIndexed { index, c ->
                val value = if (c == 'X') 10 else c.digitToInt()
                value * (10 - index)
            }.sum()

            return sum % 11 == 0
        }

        private fun isValidISBN13(isbn: String): Boolean {
            if (!isbn.matches(Regex("""^\d{13}$""")))
                return false

            val sum = isbn.mapIndexed { index, c ->
                val digit = c.digitToInt()
                if (index % 2 == 0) digit else digit * 3
            }.sum()

            return sum % 10 == 0
        }
    }

    override fun toString(): String = value
}