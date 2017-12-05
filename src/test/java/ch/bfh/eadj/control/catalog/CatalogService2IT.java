package ch.bfh.eadj.control.catalog;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.bfh.eadj.control.exception.BookAlreadyExistsException;
import ch.bfh.eadj.control.exception.BookNotFoundException;
import ch.bfh.eadj.entity.Book;

@RunWith(Arquillian.class)
public class CatalogService2IT {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(CatalogService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Inject
    CatalogService catalogService;


    @Test
    public void shouldAddBook() throws BookAlreadyExistsException, BookNotFoundException {
        Book b = createBook();

        em.getTransaction().begin();
        catalogService.addBook(b);
        em.getTransaction().commit();


        em.getTransaction().begin();
        catalogService.findBook(b.getIsbn());
        em.getTransaction().commit();

    }



    private Book createBook() {
        Book b = new Book();
        b.setTitle("test");
        b.setIsbn("12345");
        b.setAuthors("max muster");
        b.setPrice(new BigDecimal("10.14"));
        return b;
    }

}
