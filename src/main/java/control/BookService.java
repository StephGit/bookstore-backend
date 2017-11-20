package control;

import entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BookService extends AbstractService<Book>{

    @PersistenceContext
    EntityManager entityManager;


    public BookService() {
        super(Book.class);
    }


    public void create(Book book) {
        entityManager.persist(book);
    }






    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
