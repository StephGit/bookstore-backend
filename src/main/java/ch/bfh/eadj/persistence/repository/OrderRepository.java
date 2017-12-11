package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.dto.OrderStatisticInfo;
import ch.bfh.eadj.persistence.entity.Order;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static ch.bfh.eadj.persistence.entity.Order.PARAM_NR;
import static ch.bfh.eadj.persistence.entity.Order.PARAM_YEAR;

@Stateless
public class OrderRepository extends AbstractRepository<Order> {

    @BookstorePersistenceUnit
    @PersistenceContext(unitName = "bookstorePU")
    EntityManager em;

    public OrderRepository() {
        super(Order.class);
    }


    public List<OrderInfo> findByCustomerAndYear(Long nr, Integer year) {
        TypedQuery<OrderInfo> query = em.createNamedQuery(Order.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_NAME, OrderInfo.class);
        query.setParameter(PARAM_YEAR, year);
        query.setParameter(PARAM_NR, nr);
        return query.getResultList();
    }

    public List<Order> findByNr(Long nr) {
        TypedQuery<Order> query = em.createNamedQuery(Order.FIND_BY_NR_QUERY.QUERY_NAME, Order.class);
        query.setParameter(PARAM_NR, nr);
        return query.getResultList();
    }

    public List<OrderStatisticInfo> getStatisticByYear(Integer year) {
        TypedQuery<OrderStatisticInfo> query = em.createNamedQuery(Order.STATISTIC_BY_YEAR_QUERY.QUERY_NAME, OrderStatisticInfo.class);
        query.setParameter(PARAM_YEAR, year);
        return query.getResultList();
    }

    public boolean deleteOrder(Long order) {
        try {
            Order orderToRemove = em.getReference(Order.class, order);
            //TODO cascading
            em.remove(orderToRemove);
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
