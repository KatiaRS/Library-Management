package application.user.domain.models

@JvmInline
value class UserName(val value: String) {
    init {
        val normalized = value.trim()
        require(normalized.isNotBlank()) { "Nome inválido: o nome não pode ser vazio." }
        require(normalized.matches(Regex("""^[A-Za-zÀ-ÿ\s'-]+$"""))) {
            "Nome inválido: use somente letras, espaços e caracteres permitidos."
        }
    }

    companion object {
        fun of(value: String): UserName = UserName(value.trim())
    }

    override fun toString(): String = value
}
