package ch.bfh.eadj.control;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.dto.OrderStatisticInfo;
import ch.bfh.eadj.entity.BookOrder;
import ch.bfh.eadj.entity.OrderStatus;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
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
    public void shouldFindByNr() throws Exception {

        //when
        OrderInfo result = orderRepository.findByNr(11L);

        //then
        assertNotNull(result);
        assertThat(result.getAmount(), is(BigDecimal.valueOf(45.24)));
    }

    @Test
    public void shouldFindByNameAndYear() throws Exception {

        //when
        List<OrderInfo> result = orderRepository.findByCustomerAndYear(4, 2017);


        //then
        assertThat(result.size(),is(1));
        OrderInfo orderInfo = result.get(0);

        assertThat(orderInfo.getStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    public void shouldNotFindByNamAndYear() throws Exception {

        //when
        List<OrderInfo> result = orderRepository.findByCustomerAndYear(1, 1950);

        //then
        assertThat(result.isEmpty(),is(true));
    }

       @Test
    public void shouldGetStatisticForYear() throws Exception {
        //when
        List<OrderStatisticInfo> result = orderRepository.getStatisticByYear(2017);

        //then
        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(7));
        long averageAmount = result.get(4).getAverageAmount().longValue();
        long totalAmount = result.get(4).getTotalAmount().longValue();
        assertThat(averageAmount, is(totalAmount/result.get(4).getPositionsCount()));
        // TODO remove when test is ok
        for (OrderStatisticInfo r: result) {
            System.out.println(r.getNr() + ", " + r.getFirstName() + ", " + r.getLastName() + ", " + r.getTotalAmount() + ", " + r.getAverageAmount() + ", " + r.getPositionsCount() );
        }
    }

    @Test
    public void shouldNotGetStatisticForYear() throws Exception {
        //when
        List<OrderStatisticInfo> result = orderRepository.getStatisticByYear(1900);

        //then
        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }
}