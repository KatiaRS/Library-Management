package application.user.domain.models

@JvmInline
value class Email(val value: String) {
    init {
        val normalized = value.trim()
        require(normalized.isNotBlank()) { "E-mail inválido: o campo não pode ser vazio." }
        require(normalized.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            "E-mail inválido: informe um endereço de e-mail válido."
        }
    }

    companion object {
        fun of(value: String): Email = Email(value.trim())
    }

    override fun toString(): String = value
}
