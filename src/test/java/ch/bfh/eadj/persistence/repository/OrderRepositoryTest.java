package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.dto.OrderStatisticInfo;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OrderRepositoryTest extends AbstractTest {

    private OrderRepository orderRepository;

    @Before
    public void before() {
        orderRepository = new OrderRepository();
        orderRepository.em = em;
    }

    @Test
    public void shouldFindByNr() {
        //given
        Long orderNr = 11L;
        Double totalAmount = 45.24;

        //when
        List<Order> result = orderRepository.findByNr(orderNr);

        //then
        assertNotNull(result);
        assertThat(result.get(0).getAmount(), is(BigDecimal.valueOf(totalAmount)));
    }

    @Test
    public void shouldFindByCustomerAndYear() {
        //given
        Long customerNr = 4L;
        Integer year = 2017;

        //when
        List<OrderInfo> result = orderRepository.findByCustomerAndYear(customerNr, year);

        //then
        assertThat(result.size(),is(1));
        OrderInfo orderInfo = result.get(0);
        assertThat(orderInfo.getStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    public void shouldNotFindByCustomerAndYear() {
        //given
        Long customerNr = 1L;
        Integer year = 1950;

        //when
        List<OrderInfo> result = orderRepository.findByCustomerAndYear(customerNr, year);

        //then
        assertThat(result.isEmpty(),is(true));
    }

       @Test
    public void shouldGetStatisticForYear() {
        //given
        Integer year = 2017;

        //when
        List<OrderStatisticInfo> result = orderRepository.getStatisticByYear(year);

        //then
        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(8));
        long averageAmount = result.get(4).getAverageAmount().longValue();
        long totalAmount = result.get(4).getTotalAmount().longValue();
        assertThat(averageAmount, is(totalAmount/result.get(4).getPositionsCount()));
    }

    @Test
    public void shouldNotGetStatisticForYear() {
        //given
        Integer year = 2010;

        //when
        List<OrderStatisticInfo> result = orderRepository.getStatisticByYear(year);

        //then
        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }
}