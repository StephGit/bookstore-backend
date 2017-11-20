package control;

import dto.BookInfo;
import entity.Book;

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
        query.setParameter("isbn",isbn);
        BookInfo result = query.getSingleResult();
        return result;
    }

    public List<BookInfo> findBooksByKeywords(List<String> keywords){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BookInfo> query = builder.createQuery(BookInfo.class);
        Root<BookInfo> root = query.from(BookInfo.class);

        List<Predicate> predicates = new LinkedList<>();
        for (String keyword : keywords) {
            predicates.add(builder.like(root.<String>get("keywords"), "%" + keyword + "%"));
        }

        return em.createQuery(
                query.select(root).where(
                        builder.or(
                                predicates.toArray(new Predicate[predicates.size()])
                        )
                ))
                .getResultList();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
