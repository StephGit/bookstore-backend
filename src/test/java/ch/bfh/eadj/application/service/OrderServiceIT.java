package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.application.exception.PaymentFailedException;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.entity.OrderItem;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.*;

public class OrderServiceIT extends AbstractServiceIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/OrderService";
    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CatalogService";
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CustomerService";

    private CustomerServiceRemote customerService;
    private CatalogServiceRemote catalogService;
    private OrderServiceRemote orderService;

    private Book book;
    private Customer customer;
    private Order order;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        orderService = (OrderServiceRemote) jndiContext.lookup(ORDER_SERVICE_NAME);
        catalogService = (CatalogServiceRemote) jndiContext.lookup(CATALOG_SERVICE_NAME);
        customerService = (CustomerServiceRemote) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test(dependsOnMethods = "shouldPlaceOrder")
    public void shouldCancelOrder() throws Exception {
        //when
        orderService.cancelOrder(order.getNr());
        order = orderService.findOrder(order.getNr());

        //then
        assertThat(order.getStatus(), is(OrderStatus.CANCELED));
    }

    @Test(dependsOnMethods = "shouldPlaceOrder")
    public void findOrder() throws Exception {
        //when
        Order orderFromDb = orderService.findOrder(order.getNr());

        //then
        assertEquals(orderFromDb.getAmount(), order.getAmount());
    }

    @Test
    public void shouldPlaceOrder() throws Exception {
        //given
        book = createBook();
        catalogService.addBook(book);
        book = catalogService.findBook(book.getIsbn());
        List<OrderItem> items = createOrderItems(3, book);
        customer = createCustomer();
        Long userId = customerService.registerCustomer(customer, "pwd");
        customer = customerService.findCustomer(userId);


        //when
        order = orderService.placeOrder(customer, items);

        //then
        assertThat(order.getStatus(), is(OrderStatus.ACCEPTED));
    }

    @Test(dependsOnMethods = "shouldPlaceOrder")
    public void shouldFailPlaceOrder() throws Exception {
        //given
        List<OrderItem> items = createOrderItems(30, book);
        try {
            //when
            order = orderService.placeOrder(customer, items);

            //then
//            assertThat(order.getAmount(), is(book.getPrice().multiply(new BigDecimal(30))));
            fail("PaymentFailedException exception");
        } catch (PaymentFailedException e) {
            System.out.println("Expected exception: PaymentFailedException");
        }
    }

    @Test(dependsOnMethods = "shouldPlaceOrder")
    public void searchOrders() throws Exception {
        //given
        Integer year = 2017;

        //when
        List<OrderInfo> orderInfoList = orderService.searchOrders(customer, year);

        //then
        assertFalse(orderInfoList.isEmpty());
        assertThat(orderInfoList.get(0).getAmount(), is(order.getAmount()));
    }

    @Test(dependsOnMethods = "shouldPlaceOrder")
    public void tearDown() throws Exception {

        customer = customerService.findCustomer(customer.getNr());
        customerService.removeCustomer(customer);
        order = orderService.findOrder(order.getNr());
        orderService.removeOrder(order);
        book = catalogService.findBook(book.getIsbn());
        catalogService.removeBook(book);
        try {
            book = catalogService.findBook(book.getIsbn());
            fail("BookNotFoundException exception");
        } catch (BookNotFoundException e) {
            System.out.println("Expected exception: BookNotFoundException");
        }
    }
}