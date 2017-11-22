package ch.bfh.eadj.control;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.entity.OrderStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OrderRepositoryTest extends AbstractTest {
    private OrderRepository orderRepository;

    @Before
    public void before() throws Exception {
        orderRepository = new OrderRepository();
        orderRepository.em = em;
    }

    @Test
    public void findByNameAndYear() throws Exception {

        //when
        List<OrderInfo> result = orderRepository.findByNameAndYear("Smith", 2017);


        //then
        assertThat(result.size(),is(1));
        OrderInfo orderInfo = result.get(0);

        assertThat(orderInfo.getStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    public void getStatisticByYear() throws Exception {


    }
}