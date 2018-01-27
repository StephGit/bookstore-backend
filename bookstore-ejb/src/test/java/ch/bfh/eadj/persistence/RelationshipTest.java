package ch.bfh.eadj.persistence;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class RelationshipTest extends AbstractTest {

    @Test
    public void shouldFindOrderOfCustomer() {

        TypedQuery<Customer> q = em.createQuery("select c from Customer c", Customer.class);
        List<Customer> listCustomer = q.getResultList();
        assertTrue(!listCustomer.isEmpty());

        TypedQuery<Order> q2 = em.createQuery("select o from Order o join o.customer c where c.nr = " + listCustomer.get(0).getNr() , Order.class);
        List<Order> listOrder = q2.getResultList();
        assertTrue(!listOrder.isEmpty());
    }

    @Test
    public void persistAfterRemove() {
        Customer customer = new Customer();
        em.persist(customer);
        assertTrue(em.contains(customer));

        em.remove(customer);
        assertFalse(em.contains(customer));

        em.persist(customer);
        assertTrue(em.contains(customer));
    }
}
