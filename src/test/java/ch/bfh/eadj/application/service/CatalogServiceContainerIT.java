package ch.bfh.eadj.application.service;

import ch.bfh.eadj.TestCDISetup;
import ch.bfh.eadj.application.service.CatalogService;
import ch.bfh.eadj.persistence.repository.BookRepository;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.entity.Book;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;

import javax.ejb.EJB;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

public class CatalogServiceContainerIT {

    @Rule
    public WeldInitiator weld = WeldInitiator.from(CatalogService.class, BookRepository.class, TestCDISetup.class).inject(this).build();


    @EJB
    private CatalogService catalogService;

    @Test
    public void shouldFindBook() throws BookNotFoundException {
        //given
        String isbn = "417998182-3";

        //when
        Book book = catalogService.findBook(isbn);

        assertEquals(isbn, book.getIsbn());
    }

}
