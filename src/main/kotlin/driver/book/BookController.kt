package driver.book

import application.book.commands.CreateBookCommand
import application.book.domain.models.BookId
import application.book.handlers.CreateBookHandler
import application.book.ports.BookRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@ApplicationScoped
@Path("/books")
class BookController {

    @Inject
    lateinit var createBookHandler: CreateBookHandler

    @Inject
    lateinit var bookRepository: BookRepository

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createBook(request: CreateBookRequest): Response {
        val created = createBookHandler.handle(
            CreateBookCommand(
                title = request.title,
                author = request.author,
                isbn = request.isbn
            )
        )

        return Response.status(Response.Status.CREATED)
            .entity(BookResponse.from(created))
            .build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listBooks(): List<BookResponse> =
        bookRepository.findAll().map(BookResponse::from)

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun findById(@PathParam("id") id: String): Response {
        val bookId = BookId.from(id)
        val book = bookRepository.findById(bookId)
            ?: return Response.status(Response.Status.NOT_FOUND).build()

        return Response.ok(BookResponse.from(book)).build()
    }
}

data class CreateBookRequest(
    val title: String,
    val author: String,
    val isbn: String
)

data class BookResponse(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String
) {
    companion object {
        fun from(book: application.book.domain.models.Book): BookResponse =
            BookResponse(
                id = book.id.value.toString(),
                title = book.title.value,
                author = book.author.name,
                isbn = book.isbn.value
            )
    }
}
