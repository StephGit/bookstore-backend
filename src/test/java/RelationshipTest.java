import entity.Order;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RelationshipTest extends AbstractTest {

    @Test
    public void findOrder() {
        Order order = em.find(Order.class, orderId);

        assertTrue(order.getOrderItems().size() > 0);
    }

}
