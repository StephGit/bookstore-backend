package ch.bfh.eadj.control;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.dto.BookInfo;
import ch.bfh.eadj.entity.Book;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class BookRepositoryTest extends AbstractTest {
    private BookRepository bookRepo;


    @Before
    public void setUpRepo() throws Exception {
        bookRepo = new BookRepository();
        bookRepo.em = em;
    }

    @Test
    public void souldFindBookByIsbn() throws Exception {
        //given
        String isbn = "417998182-3";

        //when
        BookInfo bookByIsbn = bookRepo.findBookByIsbn(isbn);

        //then
        assertEquals(isbn, bookByIsbn.getIsbn());
        assertEquals(bookByIsbn.getTitle(),"Far Side of the Moon, The (Face cachée de la lune, La)");
        assertEquals(bookByIsbn.getAuthors(),"Betsey Kindred");
        assertThat(bookByIsbn.getPrice(),is(new BigDecimal("85.22")));
    }

    @Test
    public void shouldFindBooksByTitleKeywords() throws Exception {

        //given
        List<String> keywords = Arrays.asList("Far Side", "Moon", "the");

        //when
        List<Book> booksByKeywords = bookRepo.findBooksByKeywords(keywords);

        //then
        assertThat(booksByKeywords.size(),is(1));
        Book foundBook = booksByKeywords.get(0);
        assertTrue(foundBook.getTitle().contains(keywords.get(0)));
        assertTrue(foundBook.getTitle().contains(keywords.get(1)));
        assertTrue(foundBook.getTitle().contains(keywords.get(2)));
    }

    @Test
    public void shouldFindBooksByAuthorKeywords() throws Exception {

        //given
        List<String> keywords = Arrays.asList("Hana");

        //when
        List<Book> booksByKeywords = bookRepo.findBooksByKeywords(keywords);

        //then
        assertThat(booksByKeywords.size(),is(2));
        Book foundBook = booksByKeywords.get(0);
        assertTrue(foundBook.getAuthors().contains(keywords.get(0)));
    }

    @Test
    public void shouldFindBooksByPublisherKeywords() throws Exception {

        //given
        List<String> keywords = Arrays.asList("Babblestorm");

        //when
        List<Book> booksByKeywords = bookRepo.findBooksByKeywords(keywords);

        //then
        assertThat(booksByKeywords.size(),is(2));
        Book foundBook = booksByKeywords.get(0);
        assertTrue(foundBook.getPublisher().contains(keywords.get(0)));
    }

}