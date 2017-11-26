package ch.bfh.eadj.control;

import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.dto.OrderStatisticInfo;
import ch.bfh.eadj.entity.BookOrder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static ch.bfh.eadj.entity.BookOrder.PARAM_NR;
import static ch.bfh.eadj.entity.BookOrder.PARAM_YEAR;

public class OrderRepository extends AbstractRepository<BookOrder> {

    @PersistenceContext
    EntityManager em;

    public OrderRepository() {
        super(BookOrder.class);
    }


    public List<OrderInfo> findByCustomerAndYear(Long nr, Integer year) {
        TypedQuery<OrderInfo> query = em.createNamedQuery(BookOrder.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_NAME, OrderInfo.class);
        query.setParameter(PARAM_YEAR, year);
        query.setParameter(PARAM_NR, nr);
        return query.getResultList();
    }

    public OrderInfo findByNr(Long nr) {
        TypedQuery<OrderInfo> query = em.createNamedQuery(BookOrder.FIND_BY_NR_QUERY.QUERY_NAME, OrderInfo.class);
        query.setParameter(PARAM_NR, nr);
        return query.getSingleResult();
    }

    public List<OrderStatisticInfo> getStatisticByYear(Integer year) {
        TypedQuery<OrderStatisticInfo> query = em.createNamedQuery(BookOrder.STATISTIC_BY_YEAR_QUERY.QUERY_NAME, OrderStatisticInfo.class);
        query.setParameter(PARAM_YEAR, year);
        return query.getResultList();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
