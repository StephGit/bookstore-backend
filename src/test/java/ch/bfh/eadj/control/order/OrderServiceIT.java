package ch.bfh.eadj.control.order;


import ch.bfh.eadj.control.catalog.CatalogServiceRemote;
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

public class OrderServiceIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/OrderService";

    private CatalogServiceRemote orderService;
    private Book book;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        orderService = (CatalogServiceRemote) jndiContext.lookup(ORDER_SERVICE_NAME);
    }


    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldAddOrder()  {
        //TODO exceptions



    }

    @Test(dependsOnMethods = "shouldCreateBook",expectedExceptions  = BookAlreadyExistsException.class)
    public void shouldFailAddBook() throws BookAlreadyExistsException {
        //when
        Book book = createBook();
        orderService.addBook(book);
    }

    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldFindBook() throws BookAlreadyExistsException, BookNotFoundException {

        //when
        Book bookFromDb = orderService.findBook(book.getIsbn());
        this.book = bookFromDb;

        //then
        assertEquals("test", bookFromDb.getTitle());
        assertEquals("max muster", bookFromDb.getAuthors());
        assertEquals("12345", bookFromDb.getIsbn());
    }

    @Test(dependsOnMethods = "shouldCreateBook",expectedExceptions  = BookNotFoundException.class)
    public void shouldNotFindBook() throws BookNotFoundException {
        //when
       orderService.findBook("999999");// not existent
    }

    @Test(dependsOnMethods = "shouldCreateBook")
    public void shouldFindBookByKeywords() throws BookAlreadyExistsException {

        //when
        List<BookInfo> booksFromDb = orderService.searchBooks("max");

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
        Book bookFromDb = orderService.findBook(book.getIsbn());
        bookFromDb.setAuthors("Adrian Krebs");

        orderService.updateBook(bookFromDb);

        //then
        Book afterUpdate = orderService.findBook(book.getIsbn());
        assertEquals("Adrian Krebs", afterUpdate.getAuthors());
    }

    @Test(dependsOnMethods = "shouldCreateBook",expectedExceptions  = BookNotFoundException.class)
    public void shouldFailUpdateBook() throws BookAlreadyExistsException, BookNotFoundException {
        //given
        Book book = createBook();
        book.setIsbn("1231231321");
        //when
        orderService.updateBook(book);
    }


    @Test
    public void shouldCreateBook() throws BookAlreadyExistsException {
        Book b = createBook();
        orderService.addBook(b);
        book = b;
    }

    private Book createBook() {
        Book b = new Book();
        b.setTitle("test");
        b.setIsbn("12345");
        b.setAuthors("max muster");
        b.setPrice(new BigDecimal("10.14"));
        return b;
    }

    @Test(dependsOnMethods = {"shouldCreateBook", "shouldUpdateBook", "shouldAddBook", "shouldFindBook"})
    public void shouldRemoveBook() throws BookAlreadyExistsException {
        orderService.removeBook(book.getNr());
    }



}