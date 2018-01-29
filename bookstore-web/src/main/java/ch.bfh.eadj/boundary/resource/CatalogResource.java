package ch.bfh.eadj.boundary.resource;

import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.application.service.CatalogService;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class CatalogResource {

    @Inject
    private CatalogService catalogService;

    @POST
    public Response addBook(Book book) {
        try {
            catalogService.addBook(book);
            return Response.status(Status.CREATED).build();
        } catch (BookAlreadyExistsException ex) {
            throw new WebApplicationException(Status.CONFLICT);
        }
    }

    @GET
    @Path("{isbn}")
    public Book findBook(@PathParam("isbn") String isbn) {
        try {
            return catalogService.findBook(isbn);
        } catch (BookNotFoundException ex) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

    @GET
    public List<BookInfo> searchBooks(@QueryParam("keywords") String keywords) {
        return catalogService.searchBooks(keywords);

    }

    @PUT
    @Path("{isbn}")
    public Response updateBook(@PathParam("isbn") String isbn, Book book) {
        validatePathAndBodyIsbn(isbn, book);

        try {
            catalogService.updateBook(book);
            return Response.status(Status.NO_CONTENT).build();
        } catch (BookNotFoundException ex) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

    private void validatePathAndBodyIsbn(String isbn, Book book) {
        if (!isbn.equals(book.getIsbn())) {
            throw new WebApplicationException("the ISBN number of the book data does not match the path parameter",
                    Status.BAD_REQUEST);
        }
    }


}
