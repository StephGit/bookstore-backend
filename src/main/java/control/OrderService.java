package control;

import dto.OrderInfo;
import entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class OrderService extends AbstractService{

    @PersistenceContext
    EntityManager em;

    public OrderService() {
        super(Order.class);
    }


    public List<OrderInfo> findByNameAndYear() {
        TypedQuery<OrderInfo> query = em.createNamedQuery(Order.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_NAME, OrderInfo.class);
        List<OrderInfo> resultList = query.getResultList();
        return resultList;
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
