package ch.bfh.eadj.control.catalog;


import ch.bfh.eadj.control.exception.BookAlreadyExistsException;
import ch.bfh.eadj.control.exception.BookNotFoundException;
import ch.bfh.eadj.dto.BookInfo;
import ch.bfh.eadj.entity.Book;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

public class CatalogServiceIT  {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CatalogService";

    private CatalogServiceRemote catalogService;
    private Book book;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogServiceRemote) jndiContext.lookup(CATALOG_SERVICE_NAME);
    }


    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldAddBook() throws BookAlreadyExistsException, BookNotFoundException {

        //then
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());

    }

    @Test(expectedExceptions  = BookAlreadyExistsException.class)
    public void shouldFailAddBook() throws BookAlreadyExistsException {
        //when
//        createBook();
//        createBook();
    }

    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldFindBook() throws BookAlreadyExistsException, BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(book.getIsbn());

        //then
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals("12345", bookFromDb.getIsbn());
    }

    @Test(expectedExceptions  = BookNotFoundException.class)
    public void shouldNotFindBook() throws BookNotFoundException {
        //when
        catalogService.findBook("12345");
    }

    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldFindBookByKeywords() throws BookAlreadyExistsException {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max");

        //then
        assertThat(booksFromDb.size(), is(1));
        BookInfo bookFromDb = booksFromDb.get(0);
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals("12345", bookFromDb.getIsbn());
    }

    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldUpdateBook() throws BookAlreadyExistsException, BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        bookFromDb.setAuthors("Adrian Krebs");

        catalogService.updateBook(bookFromDb);

        //then
        Book afterUpdate = catalogService.findBook(book.getIsbn());
        assertEquals("Adrian Krebs", afterUpdate.getAuthors());
    }

    @Test(expectedExceptions  = BookNotFoundException.class)
    public void shouldFailUpdateBook() throws BookAlreadyExistsException, BookNotFoundException {
        //given
        Book unknown = new Book();
        unknown.setAuthors("Adrian Krebs");

        //when
        catalogService.updateBook(unknown);
    }


    @Test
    public void shouldCreateBook() throws BookAlreadyExistsException {
        Book b = new Book();
        b.setTitle("test");
        b.setIsbn("12345");
        b.setAuthors("max muster");
        b.setPrice(new BigDecimal("10.14"));
        catalogService.addBook(b);
        book = b;
    }


}