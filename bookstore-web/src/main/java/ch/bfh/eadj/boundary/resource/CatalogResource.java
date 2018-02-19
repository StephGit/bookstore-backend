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

import java.lang.annotation.Repeatable;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("books")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class CatalogResource {

    @Inject
    private CatalogService catalogService;

    /**
     * Adds a book to the catalog.
     * @param body the data of the book to be added
     * @responseMessage 201 the addition was successful
     * @responseMessage 409 a book with the same ISBN number already exists
     */
    @POST
    public Response addBook(Book body) {
        try {
            catalogService.addBook(body);
            return Response.status(Status.CREATED).build();
        } catch (BookAlreadyExistsException ex) {
            throw new WebApplicationException(Status.CONFLICT);
        }
    }

    /**
     * Finds a book by its ISBN number and returns the data of the found book.
     * @param isbn the ISBN number of the book
     * @responseMessage 200 the retrieval was successful
     * @responseMessage 404 no book with the specified ISBN number exists
     */
    @GET
    @Path("{isbn}")
    public Response findBook(@PathParam("isbn") String isbn) {
        try {
            Book book = catalogService.findBookOnAmazon(isbn);
            return Response.status(Status.OK).entity(book).build();
        } catch (BookNotFoundException ex) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

    /**
     * Searches for books by keywords and returns a list of matching books.
     * @description A book is included in the result if every keyword is contained in the title, authors or publisher field.
     * @param keywords the keywords to search for
     * @responseMessage 200 the search was successful
     */
    @GET
    public List<BookInfo> searchBooks(@QueryParam("keywords") String keywords) {
        return catalogService.searchBooks(keywords);

    }

    /**
     * Updates the data of a book.
     * @param isbn the ISBN number of the book
     * @param body the data of the book to be updated
     * @responseMessage 204 the update was successful
     * @responseMessage 400 the ISBN number of the book data does not match the path parameter
     * @responseMessage 404 no book with the specified ISBN number exists
     */
    @PUT
    @Path("{isbn}")
    public Response updateBook(@PathParam("isbn") String isbn, Book body) {
        validatePathAndBodyIsbn(isbn, body);

        try {
            catalogService.updateBook(body);
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
