package application.book.domain.models

class Title private constructor(
    val value: String
) {
    init {
        require(value.isNotBlank()) { "Título não pode ser vazio" }
    }

    override fun toString(): String = value

    companion object {
        fun of(value: String): Title {
            return Title(value.trim())
        }
    }
}