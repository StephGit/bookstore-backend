package ch.bfh.eadj.control;

import ch.bfh.eadj.entity.Customer;
import ch.bfh.eadj.entity.Order;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class RelationshipTest extends ch.bfh.eadj.control.AbstractTest {

    @Test
    public void findOrder() {
        Order order = em.find(Order.class, orderId);

        assertTrue(order.getOrderItems().size() > 0);
    }

    @Test
    public void shouldFindOrderOfCustomer() {

        TypedQuery<Customer> q = em.createQuery("select c from Customer c", Customer.class);
        List<Customer> listCustomer = q.getResultList();
        assertTrue(!listCustomer.isEmpty());

        TypedQuery<Order> q2 = em.createQuery("select o from Order o join o.customer c where c.nr =" + listCustomer.get(0).getNr() , Order.class);
        List<Order> listOrder = q2.getResultList();
        assertTrue(!listOrder.isEmpty());
    }


}
