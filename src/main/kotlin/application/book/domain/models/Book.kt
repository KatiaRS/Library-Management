package application.book.domain.models

class Book(
    val id: BookId,
    val title: Title,
    val author: Author,
    val isbn: Isbn
) {
    init {
        require(title.value.isNotBlank()) { "Título não pode ser vazio" }
        require(author.name.isNotBlank()) { "Author name cannot be blank" }
        require(isbn.value.isNotBlank()) { "ISBN não pode ser vazio" }
    }

    companion object {
        fun create(
            title: Title,
            author: Author,
            isbn: Isbn,
            id: BookId = BookId()
        ): Book = Book(id, title, author, isbn)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Book) return false

        return id == other.id && title == other.title && author == other.author && isbn == other.isbn
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + isbn.hashCode()
        return result
    }

    override fun toString(): String {
        return "Book(id=${id.value}, title=${title.value}, author=${author.name}, isbn=${isbn.value})"
    }
}
