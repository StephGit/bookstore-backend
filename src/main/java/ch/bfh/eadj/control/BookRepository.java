package ch.bfh.eadj.control;

import ch.bfh.eadj.dto.BookInfo;
import ch.bfh.eadj.entity.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class BookRepository extends AbstractRepository<Book> {

    @PersistenceContext
    EntityManager em;


    public BookRepository() {
        super(Book.class);
    }


    public BookInfo findBookByIsbn(String isbn) {
        TypedQuery<BookInfo> query = em.createNamedQuery(Book.FIND_BY_ISBN_QUERY.QUERY_NAME, BookInfo.class);
        query.setParameter("isbn", isbn);
        BookInfo result = query.getSingleResult();
        return result;
    }

    public List<Book> findBooksByKeywords(List<String> keywords) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        List<Predicate> titlePredicates = new LinkedList<>();
        List<Predicate> authorPredicates = new LinkedList<>();
        List<Predicate> publisherPredicates = new LinkedList<>();


        for (String keyword : keywords) {
            titlePredicates.add(builder.like(root.<String>get("title"), "%" + keyword + "%"));
            authorPredicates.add(builder.like(root.<String>get("authors"), "%" + keyword + "%"));
            publisherPredicates.add(builder.like(root.<String>get("publisher"), "%" + keyword + "%"));
        }


        Predicate title = builder.and((titlePredicates.toArray(new Predicate[titlePredicates.size()])));
        Predicate author = builder.and((authorPredicates.toArray(new Predicate[authorPredicates.size()])));
        Predicate publisher = builder.and((publisherPredicates.toArray(new Predicate[publisherPredicates.size()])));

        return em.createQuery(
                query.select(root).where(
                        builder.or(
                                title, author, publisher

                        )
                ))
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
