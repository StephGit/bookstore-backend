package ch.bfh.eadj.application.service;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.TestCDISetup;
import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.repository.BookRepository;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(WeldJunit5Extension.class)
public class CatalogServiceContainerIT {

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(CatalogService.class, BookRepository.class, TestCDISetup.class, AmazonCatalog.class).inject(this).build();

    @BookstorePersistenceUnit
    @Inject
    EntityManager em;

    @Inject
    private CatalogService catalogService;


    @BeforeEach
    public void setup() {
        catalogService.bookRepo.em = em;
    }


    @Test
    public void shouldFindBook() throws BookNotFoundException {
        //given
        String isbn = "864930464-8";

        //when
        Book book = catalogService.findBook(isbn);

        assertEquals(isbn, book.getIsbn());
    }

    @Test
    public void shouldCreateBook() throws BookNotFoundException, BookAlreadyExistsException {
        //given
        String isbn = "99999999";

        Book book = new Book();
        book.setIsbn(isbn);
        book.setAuthors("Luna");
        book.setTitle("Lena");
        book.setPrice(new BigDecimal("100.00"));

        //when
        em.getTransaction().begin();
        catalogService.addBook(book);
        em.getTransaction().commit();

        em.getTransaction().begin();
        Book bookFromDb = catalogService.findBook(isbn);
        em.getTransaction().commit();
        em.close();

        assertEquals(isbn, bookFromDb.getIsbn());
    }

}
