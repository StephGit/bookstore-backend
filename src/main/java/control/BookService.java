package control;

import dto.BookInfo;
import entity.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class BookService extends AbstractService<Book> {

    @PersistenceContext
    EntityManager em;


    public BookService() {
        super(Book.class);
    }


    public BookInfo findBookByIsbn(String isbn) {
        TypedQuery<BookInfo> query = em.createNamedQuery(Book.FIND_BY_ISBN_QUERY.QUERY_NAME, BookInfo.class);
        query.setParameter("isbn",isbn);
        BookInfo result = query.getSingleResult();
        return result;
    }

    public List<BookInfo> findBooksbyKeywords(Book book) {
        TypedQuery<BookInfo> query = em.createNamedQuery(Book.FIND_BY_KEYWORD_QUERY.QUERY_NAME, BookInfo.class);
        List<BookInfo> resultList = query.getResultList();
        return resultList;
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
