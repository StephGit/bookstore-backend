package ch.bfh.eadj.application.service;


import ch.bfh.eadj.TestCDISetup;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.entity.OrderItem;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.persistence.repository.BookRepository;
import ch.bfh.eadj.persistence.repository.CustomerRepository;
import ch.bfh.eadj.persistence.repository.LoginRepository;
import ch.bfh.eadj.persistence.repository.OrderRepository;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;
import org.testng.annotations.BeforeClass;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OrderServiceContainerIT extends AbstractServiceIT {

    @Rule
    public WeldInitiator weld = WeldInitiator.from(
            OrderService.class,
            OrderRepository.class,
            CustomerService.class,
            CustomerRepository.class,
            LoginRepository.class,
            CatalogService.class,
            BookRepository.class,
            TestCDISetup.class).inject(this).build();

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/OrderService";

    @Inject
    private OrderService orderService;

    @Inject
    private CustomerService customerService;

    @Inject
    private CatalogService catalogService;



    @BeforeClass
    public void setUp() throws Exception {

    }

    @Test
    public void cancelOrder() throws Exception {

    }

    @Test
    public void findOrder() throws Exception {
    }

    @Test
    public void shouldPlaceOrder() throws Exception {
        //given
        Book book = catalogService.findBook("513011059-5");
        List<OrderItem> items = createOrderItems(3, book);
        Customer customer = customerService.findCustomer(2L);

        //when
        Order order = orderService.placeOrder(customer, items);

        //then
        assertThat(order.getStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test
    public void searchOrders() throws Exception {
    }

}