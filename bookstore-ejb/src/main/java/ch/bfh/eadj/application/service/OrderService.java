package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.*;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.entity.*;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.persistence.repository.OrderRepository;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import java.lang.IllegalStateException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@LocalBean
@Stateless(name = "OrderService")
public class OrderService implements OrderServiceRemote {

    private static final String CONNECTION_FACTORY_NAME = "java:jboss/exported/jms/RemoteConnectionFactory";
    private static final String QUEUE_NAME = "java:/jboss/exported/jms/queue/orderQueue";

    @Inject
    OrderRepository orderRepo;

    @Inject
    CatalogService catalogService;

    @Inject
    @JMSConnectionFactory(CONNECTION_FACTORY_NAME)
    JMSContext jmsContext;

    @Resource(lookup = QUEUE_NAME)
    private Queue orderQueue;

    @Resource(name = "paymentLimit")
    private
    Long PAYMENT_LIMIT;


    @Override
    public void cancelOrder(Long nr)
            throws OrderNotFoundException, OrderAlreadyShippedException, OrderProcessingException, OrderAlreadyCanceledException {
        Order order = findOrder(nr);
        if (OrderStatus.SHIPPED.equals(order.getStatus())) {
            throw new OrderAlreadyShippedException();
        } else if (OrderStatus.CANCELED.equals(order.getStatus())) {
            throw new OrderAlreadyCanceledException();
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepo.edit(order);
        sendMessage(order);
    }

    private void calculateOrderAmount(Order order) {
        BigDecimal orderAmount = new BigDecimal(0);
        for (OrderItem orderItem : order.getItems()) {
            orderAmount = orderAmount.add(orderItem.getBook().getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setAmount(orderAmount);
    }

    private void copyCustomerInfos(Customer customer, Order order) {
        order.setCreditCard(customer.getCreditCard());
        order.setAddress(customer.getAddress());
    }

    @Override
    public Order findOrder(Long nr) throws OrderNotFoundException {
        List<Order> orders = orderRepo.findByNr(nr);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotFoundException();
        }
        return orders.get(0);
    }

    private boolean isPaymentLimitExceeded(Order order) {
        return order.getAmount().compareTo(new BigDecimal(PAYMENT_LIMIT)) > 0;
    }

    private boolean isCreditCardExpired(LocalDate now, CreditCard creditCard) {
        return creditCard.getExpirationYear() < now.getYear() && creditCard.getExpirationMonth() < now.getMonthValue();
    }

    @Override
    public Order placeOrder(Customer customer, List<OrderItem> items) throws PaymentFailedException, OrderProcessingException, BookNotFoundException {
        storeBooksIfNotPresent(items);
        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(new HashSet<>(items));
        order.setDate(new java.util.Date());
        copyCustomerInfos(customer, order);
        calculateOrderAmount(order);
        validateOrderPlacement(order);
        order.setStatus(OrderStatus.ACCEPTED);
        orderRepo.create(order);
        sendMessage(order);
        return order;
    }

    void storeBooksIfNotPresent(List<OrderItem> items) throws BookNotFoundException {
        for (OrderItem item : items) {
            Book book = item.getBook();
            if (book != null) {
                Book bookFromDb = catalogService.findBook(book.getIsbn());
                if (bookFromDb == null) {
                    try {
                        Book bookFromAmazon = catalogService.findBookOnAmazon(book.getIsbn());
                        catalogService.addBook(bookFromAmazon);
                        item.setBook(bookFromAmazon);
                    } catch (BookAlreadyExistsException e1) {
                        throw new IllegalStateException("should not happen.... book is not supposed to be in DB yet");
                    }

                } else {
                    item.setBook(bookFromDb);
                }
            }
        }
    }

    private void sendMessage(Order order) throws OrderProcessingException {
        MapMessage message = jmsContext.createMapMessage();
        try {
            message.setJMSType(order.getStatus().toString());
            message.setLong("orderNr", order.getNr());
            jmsContext.createProducer().send(orderQueue, message);
        } catch (JMSException e) {
            throw new OrderProcessingException(e);
        }
    }

    @Override
    public void removeOrder(Order order) {
        orderRepo.remove(order);
    }


    @Override
    public List<OrderInfo> searchOrders(Customer customer, Integer year) {
        return orderRepo.findByCustomerAndYear(customer.getNr(), year);
    }

    private void validateOrderPlacement(Order order) throws PaymentFailedException {
        LocalDate now = LocalDate.now();
        CreditCard creditCard = order.getCreditCard();
        if (isCreditCardExpired(now, creditCard)) {
            throw new PaymentFailedException(PaymentFailedException.Code.CREDIT_CARD_EXPIRED);
        }

        if (isPaymentLimitExceeded(order)) {
            throw new PaymentFailedException(PaymentFailedException.Code.PAYMENT_LIMIT_EXCEEDED);
        }

        if (creditCard.getNumber().length() != 16) {
            throw new PaymentFailedException(PaymentFailedException.Code.INVALID_CREDIT_CARD);
        }
    }

}
