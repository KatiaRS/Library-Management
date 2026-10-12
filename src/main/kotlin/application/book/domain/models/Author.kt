package application.book.domain.models

data class Author(val name: String ){
    init {
        check(name.isNotBlank()) { "Author name cannot be blank" }
    }
}
