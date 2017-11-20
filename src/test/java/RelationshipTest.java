import entity.Order;
import org.junit.Test;

import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class RelationshipTest extends AbstractTest {

    @Test
    public void findOrder() {
        Order order = em.find(Order.class, orderId);

        assertTrue(order.getOrderItems().size() > 0);
    }

    @Test
    public void shouldFindOrderOfCustomer() {
        TypedQuery<Order> q = em.createQuery("select o from Order o join o.customer c where c.nr = 26", Order.class);
        List<Order> list = q.getResultList();
        assertTrue(!list.isEmpty());
    }

}
