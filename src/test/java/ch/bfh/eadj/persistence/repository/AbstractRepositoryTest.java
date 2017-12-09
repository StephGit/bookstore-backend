package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.entity.Book;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractRepositoryTest extends AbstractTest {
    private BookRepository bookRepo;
    @Before
    public void setUpRepo() throws Exception {
        bookRepo = new BookRepository();
        bookRepo.em = em;
    }



    @Test
    public void shouldCreateAndFindBook() {

        //given
        Book book = new Book();
        book.setIsbn("12312341");
        book.setAuthors("Luna");
        book.setTitle("Lena");
        book.setPrice(new BigDecimal("100.00"));

        //when
        em.getTransaction().begin();
        bookRepo.create(book);
        em.flush();
        Book bookFromDb = bookRepo.find(book.getNr());

        //then
        assertEquals(book.getIsbn(), bookFromDb.getIsbn());

    }


    @Test
    public void shouldGetBookCount() {

        //given
        int numberOfBooks = 20;

        //when
        int count = bookRepo.count();

        //then
        assertEquals(numberOfBooks, count);

    }

    @Test
    public void shouldGetAllBooks() {

        //given
        int numberOfBooks = 20;

        //when
        List<Book> all = bookRepo.getAll();

        //then
        assertEquals(numberOfBooks, all.size());

    }


}
