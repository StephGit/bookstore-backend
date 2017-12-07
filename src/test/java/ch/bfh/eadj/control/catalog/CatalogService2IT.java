package ch.bfh.eadj.control.catalog;

import ch.bfh.eadj.control.exception.BookNotFoundException;
import ch.bfh.eadj.entity.Book;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;

public class CatalogService2IT {

    @Rule
    public WeldInitiator weld = WeldInitiator.of(CatalogService.class);


    @PersistenceContext(name = "bookstoreTestPU")
    EntityManager em;

    @Inject
    CatalogService catalogService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testMyBean() throws BookNotFoundException {
        String isbn = "417998182-3";
        Book book = catalogService.findBook(isbn);

        assertEquals(isbn, book.getIsbn());
    }



}
