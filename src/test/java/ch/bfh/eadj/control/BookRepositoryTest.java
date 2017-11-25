package ch.bfh.eadj.control;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.dto.BookInfo;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BookRepositoryTest extends AbstractTest {
    private BookRepository bookRepo;


    @Before
    public void setUpRepo() throws Exception {
        bookRepo = new BookRepository();
        bookRepo.em = em;
    }

    @Test
    public void findBookByIsbn() throws Exception {
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
    public void findBooksByKeywords() throws Exception {
    }

}