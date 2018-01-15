package ch.bfh.eadj.application.service;


import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CatalogServiceIT extends AbstractServiceIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CatalogService";

    private static CatalogServiceRemote catalogService;
    private Book book;
    private Book secondBook;

    @BeforeClass
    public static void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        catalogService = (CatalogServiceRemote) jndiContext.lookup(CATALOG_SERVICE_NAME);
    }

    @Before
    public void shouldAddBook() throws BookAlreadyExistsException, BookNotFoundException {
        //given
        Book b = createBook("test",  Integer.toString(new Random().nextInt(10000)), "max muster");
        Book b2 = createBook("girod der knecht", Integer.toString(new Random().nextInt(10000)), "sven muster");


        //when
        catalogService.addBook(b);
        catalogService.addBook(b2);
        book = b;
        secondBook = b2;

        //then
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        Book book2FromDb = catalogService.findBook(secondBook.getIsbn());
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());
        assertEquals(secondBook.getIsbn(), book2FromDb.getIsbn());
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void shouldFailAddBook() throws BookAlreadyExistsException {
        //when
        Book book = createBook("test", this.book.getIsbn(), "max muster");
        catalogService.addBook(book);
    }

    @Test
    public void shouldFindBook() throws BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        this.book = bookFromDb;

        //then
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
    }

    @Test(expected = BookNotFoundException.class)
    public void shouldNotFindBook() throws BookNotFoundException {
        //when
       catalogService.findBook("999999");// not existent
    }

    @Test
    public void shouldFindBookByKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max");

        //then
        assertThat(booksFromDb.size(), is(1));
        BookInfo bookFromDb = booksFromDb.get(0);
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());
    }

    @Test
    public void shouldFindBookByTwoKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max test");

        //then
        assertThat(booksFromDb.size(), is(1));
        BookInfo bookFromDb = booksFromDb.get(0);
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());
    }

    @Test
    public void shouldFindBookByKeywordsCaseInsensitive() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("MAX");

        //then
        assertThat(booksFromDb.size(), is(1));
        BookInfo bookFromDb = booksFromDb.get(0);
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());
    }



    @Test
    public void shouldNotFindNonExistingBookWithMultipleKeywords() throws BookNotFoundException {


        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("max sven");

        //then
        assertThat(booksFromDb.size(), is(0));


    }



    @Test
    public void shouldNotFindBookByKeywords() {

        //when
        List<BookInfo> booksFromDb = catalogService.searchBooks("Mani");

        //then
        assertTrue(booksFromDb.isEmpty());
    }

    @Test
    public void shouldUpdateBook() throws BookNotFoundException {

        //when
        Book bookFromDb = catalogService.findBook(book.getIsbn());
        bookFromDb.setAuthors("Adrian Krebs");

        catalogService.updateBook(bookFromDb);

        //then
        Book afterUpdate = catalogService.findBook(book.getIsbn());
        assertEquals("Adrian Krebs", afterUpdate.getAuthors());
    }

    @Test(expected = BookNotFoundException.class)
    public void shouldFailUpdateBook() throws BookNotFoundException {
        //given
        Book book = createBook("test", "12345", "max muster");
        book.setIsbn("1231231321");
        //when
        catalogService.updateBook(book);
    }

    @After
    public void shouldRemoveBook() throws BookNotFoundException {
        book = catalogService.findBook(book.getIsbn());
        catalogService.removeBook(book);
        secondBook = catalogService.findBook(secondBook.getIsbn());
        catalogService.removeBook(secondBook);


    }



}