package ch.bfh.eadj.control;

import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.dto.OrderStatisticInfo;
import ch.bfh.eadj.entity.BookOrder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class OrderRepository extends AbstractRepository<BookOrder> {

    @PersistenceContext
    EntityManager em;

    public OrderRepository() {
        super(BookOrder.class);
    }


    public List<OrderInfo> findByCustomerAndYear(Long nr, Integer year) {
        TypedQuery<OrderInfo> query = em.createNamedQuery(BookOrder.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_NAME, OrderInfo.class);
        query.setParameter("year", year);
        query.setParameter("nr", nr); //TODO constants
        return query.getResultList();
    }

    public OrderInfo findByNr(Long nr) {
        TypedQuery<OrderInfo> query = em.createNamedQuery(BookOrder.FIND_BY_NR_QUERY.QUERY_NAME, OrderInfo.class);
        query.setParameter("nr", nr);
        return query.getSingleResult();
    }

    public List<OrderStatisticInfo> getStatisticByYear(Integer year) {
        TypedQuery<OrderStatisticInfo> query = em.createNamedQuery(BookOrder.STATISTIC_BY_YEAR_QUERY.QUERY_NAME, OrderStatisticInfo.class);
        query.setParameter("year", year);
        return query.getResultList();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
