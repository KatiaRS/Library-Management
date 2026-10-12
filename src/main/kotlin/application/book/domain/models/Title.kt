package application.book.domain.models

import org.hibernate.validator.constraints.ISBN

data class Title(
    val value: String
) {

    companion object {

        fun of(value: String): ISBN {

            val normalized = value
                .replace("-", "")
                .replace(" ", "")

            require(isValid(normalized)) {
                "ISBN inválido"
            }

            return ISBN(normalized)
        }

        private fun isValid(isbn: String): Boolean {
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
}