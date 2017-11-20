package ch.bfh.eadj.control;

import org.junit.Before;
import org.junit.Test;

public class OrderRepositoryTest extends ch.bfh.eadj.control.AbstractTest {
    private OrderRepository orderRepository;

    @Before
    public void before() throws Exception {
        orderRepository = new OrderRepository();
        orderRepository.em = em;
    }

    @Test
    public void findByNameAndYear() throws Exception {
    }

    @Test
    public void getStatisticByYear() throws Exception {
    }
}