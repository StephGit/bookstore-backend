package ch.bfh.eadj.control.order;

import ch.bfh.eadj.control.AbstractRepository;
import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.dto.OrderStatisticInfo;
import ch.bfh.eadj.entity.Order;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static ch.bfh.eadj.entity.Order.PARAM_NR;
import static ch.bfh.eadj.entity.Order.PARAM_YEAR;

public class OrderRepository extends AbstractRepository<Order> {

    @PersistenceContext
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

    public Order findByNr(Long nr) {
        TypedQuery<Order> query = em.createNamedQuery(Order.FIND_BY_NR_QUERY.QUERY_NAME, Order.class);
        query.setParameter(PARAM_NR, nr);
        return query.getSingleResult(); //TODO dont do that
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
