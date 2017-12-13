package ch.bfh.eadj.application.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.TestCDISetup;
import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.repository.BookRepository;

public class CatalogServiceContainerIT {

    @Rule
    public WeldInitiator weld = WeldInitiator.from(CatalogService.class, BookRepository.class, TestCDISetup.class).inject(this).build();

    @BookstorePersistenceUnit
    @PersistenceContext(unitName = "bookstorePU")
    EntityManager em;

    @EJB
    private CatalogService catalogService;



    @Before
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
