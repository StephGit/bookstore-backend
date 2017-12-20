package ch.bfh.eadj.application.service;


import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CatalogServiceIT extends AbstractServiceIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CatalogService";

    private CatalogServiceRemote catalogService;
    private Book book;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogServiceRemote) jndiContext.lookup(CATALOG_SERVICE_NAME);
    }

    @Test
    public void shouldAddBook() throws BookAlreadyExistsException, BookNotFoundException {
        //given
        Book b = createBook();

        //when
        catalogService.addBook(b);
        book = b;

        //then
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());
    }

    @Test(dependsOnMethods = "shouldAddBook", expectedExceptions  = BookAlreadyExistsException.class)
    public void shouldFailAddBook() throws BookAlreadyExistsException {
        //when
        Book book = createBook();
        catalogService.addBook(book);
    }

    @Test(dependsOnMethods = "shouldAddBook")
    public void shouldFindBook() throws BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        this.book = bookFromDb;

        //then
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals("12345", bookFromDb.getIsbn());
    }

    @Test(dependsOnMethods = "shouldAddBook", expectedExceptions  = BookNotFoundException.class)
    public void shouldNotFindBook() throws BookNotFoundException {
        //when
       catalogService.findBook("999999");// not existent
    }

    @Test(dependsOnMethods = "shouldAddBook")
    public void shouldFindBookByKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max");

        //then
        assertThat(booksFromDb.size(), is(1));
        BookInfo bookFromDb = booksFromDb.get(0);
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals("12345", bookFromDb.getIsbn());
    }

    @Test
    public void shouldNotFindBookByKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("Mani");

        //then
        assertTrue(booksFromDb.isEmpty());
    }

    @Test(dependsOnMethods = "shouldAddBook")
    public void shouldUpdateBook() throws BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        bookFromDb.setAuthors("Adrian Krebs");

        catalogService.updateBook(bookFromDb);

        //then
        Book afterUpdate = catalogService.findBook(book.getIsbn());
        assertEquals("Adrian Krebs", afterUpdate.getAuthors());
    }

    @Test(dependsOnMethods = "shouldAddBook",expectedExceptions  = BookNotFoundException.class)
    public void shouldFailUpdateBook() throws BookNotFoundException {
        //given
        Book book = createBook();
        book.setIsbn("1231231321");
        //when
        catalogService.updateBook(book);
    }

    @Test(dependsOnMethods = {"shouldAddBook", "shouldUpdateBook", "shouldFindBook"})
    public void shouldRemoveBook() throws BookNotFoundException {
        book = catalogService.findBook(book.getIsbn());
        catalogService.removeBook(book);
    }



}