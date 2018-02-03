package ch.bfh.eadj.application.service;


import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogServiceIT extends AbstractServiceIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-app-1.0-SNAPSHOT/bookstore/CatalogService!ch.bfh.eadj.application.service.CatalogServiceRemote";

    private static CatalogServiceRemote catalogService;
    private String isbn = "0099590085";
    private Book book;
    private Book secondBook;

    @BeforeAll
    static void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogServiceRemote) jndiContext.lookup(CATALOG_SERVICE_NAME);
    }

    @Test
    public void shouldFailAddBook() throws BookAlreadyExistsException {
        //when
        Book book = createBook("test", Integer.toString(new Random().nextInt(10000)), "max muster");
        catalogService.addBook(book);

        Executable executable = () -> catalogService.addBook(book);
        assertThrows(BookAlreadyExistsException.class, executable);
    }

    @Test
    public void shouldFindBook() throws BookNotFoundException {

        //when
        Book book = catalogService.findBookOnAmazon(isbn);

        //then
        assertEquals("Sapiens: A Brief History of Humankind", book.getTitle());
        assertEquals(isbn, book.getIsbn());
    }

    @Test
    public void shouldNotFindBook() throws BookNotFoundException {
        //when
        Executable executable = () -> catalogService.findBookOnAmazon("999999");
        assertThrows(BookNotFoundException.class, executable);
    }

    @Test
    public void shouldFindBookByKeywordsManyResults() {

        //when
        List<BookInfo> books = catalogService.searchBooks("sapiens");

        //then
        assertThat(books.size(), is(greaterThan(80)));
        BookInfo first = books.get(0);
        assertNotNull(first.getTitle());
        assertNotNull(first.getAuthors());
        assertNotNull(first.getIsbn());
        assertNotNull(first.getPrice());
    }

    @Test
    public void shouldFindBookByKeywordsFewResults() {

        //when
        List<BookInfo> books = catalogService.searchBooks("Sapiens: A Brief History of Humankind Yuval Noah Harari");

        //then
        assertThat(books.size(), is(greaterThan(5)));
        BookInfo first = books.get(0);
        assertNotNull(first.getTitle());
        assertNotNull(first.getAuthors());
        assertNotNull(first.getIsbn());
        assertNotNull(first.getPrice());
    }



    @Test
    public void shouldNotFindNonExistingBookWithMultipleKeywords() throws BookNotFoundException {


        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max sven");

        //then
        assertFalse(booksFromDb.isEmpty());
    }



    @Test
    public void shouldNotFindBookByKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("Manikrz");

        //then
        assertTrue(booksFromDb.isEmpty());
    }

    @Test
    public void shouldUpdateBook() throws BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(isbn);
        bookFromDb.setAuthors("Adrian Krebs");

        catalogService.updateBook(bookFromDb);

        //then
        Book afterUpdate = catalogService.findBook(isbn);
        assertEquals("Adrian Krebs", afterUpdate.getAuthors());
    }

    @Test
    public void shouldFailUpdateBook() throws BookNotFoundException {
        //given
        Book book = createBook("test", "12345", "max muster");
        book.setIsbn("1231231321");
        //when
        Executable executable = () -> catalogService.updateBook(book);
        assertThrows(BookNotFoundException.class, executable);
    }




}