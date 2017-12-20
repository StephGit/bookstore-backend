package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ch.bfh.eadj.persistence.entity.Book.FIND_BY_ISBN_QUERY.PARAM_ISBN;

@Stateless
public class BookRepository extends AbstractRepository<Book> {

    @Inject
    @BookstorePersistenceUnit
    public EntityManager em;


    public BookRepository() {
        super(Book.class);
    }

    public List<Book> findByIsbn(String isbn) {
        TypedQuery<Book> query = em.createNamedQuery(Book.FIND_BY_ISBN_QUERY.QUERY_NAME, Book.class);
        query.setParameter(PARAM_ISBN, isbn);
        return query.getResultList();
    }

    public List<BookInfo> findByKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty() || keywords.get(0).isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BookInfo> query = builder.createQuery(BookInfo.class);
        Root<Book> root = query.from(Book.class);

        List<Predicate> titlePredicates = new LinkedList<>();
        List<Predicate> authorPredicates = new LinkedList<>();
        List<Predicate> publisherPredicates = new LinkedList<>();


        for (String keyword : keywords) {
            titlePredicates.add(builder.like(root.get("title"), "%" + keyword + "%"));
            authorPredicates.add(builder.like(root.get("authors"), "%" + keyword + "%"));
            publisherPredicates.add(builder.like(root.get("publisher"), "%" + keyword + "%"));
        }


        Predicate title = builder.and((titlePredicates.toArray(new Predicate[titlePredicates.size()])));
        Predicate author = builder.and((authorPredicates.toArray(new Predicate[authorPredicates.size()])));
        Predicate publisher = builder.and((publisherPredicates.toArray(new Predicate[publisherPredicates.size()])));

        return em.createQuery(
                query.select(builder.construct(BookInfo.class, root.get("isbn"), root.get("authors"), root.get("title"), root.get("price"))).where(
                        builder.or(
                                title, author, publisher

                        )
                ))
                .getResultList();
    }

    public boolean deleteBook(Long bookNr) {
        try {
            Book bookToRemove = em.getReference(Book.class, bookNr);
            em.remove(bookToRemove);
            return true;
        } catch (EntityExistsException ex ) {
            return false;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
