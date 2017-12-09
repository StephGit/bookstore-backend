package ch.bfh.eadj.control.catalog;

import ch.bfh.eadj.TestCDISetup;
import ch.bfh.eadj.control.BookRepository;
import ch.bfh.eadj.control.exception.BookNotFoundException;
import ch.bfh.eadj.entity.Book;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class CatalogServiceContainerIT {

    @Rule
    public WeldInitiator weld = WeldInitiator.from(CatalogService.class, BookRepository.class, TestCDISetup.class).inject(this).build();


    @Inject
    CatalogService catalogService;

    @Test
    public void shouldFindBook() throws BookNotFoundException {
        //given
        String isbn = "417998182-3";

        //when
        Book book = catalogService.findBook(isbn);

        assertEquals(isbn, book.getIsbn());
    }

}
