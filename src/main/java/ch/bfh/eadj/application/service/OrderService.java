package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.OrderAlreadyShippedException;
import ch.bfh.eadj.application.exception.OrderNotFoundException;
import ch.bfh.eadj.application.exception.PaymentFailedException;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.entity.*;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.persistence.repository.OrderRepository;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Stateless
public class OrderService implements OrderServiceRemote {

    @Inject
    private OrderRepository orderRepo;

    //TODO store config in .properites or xml file and retrieve it from there
    // ejb.jar does not work because we package our application into a war
    @Resource(name="jndi.properties/withdrawLimit")
    private BigDecimal PAYMENT_LIMIT;


    @Override
    public void cancelOrder(Long nr) throws OrderNotFoundException, OrderAlreadyShippedException {
        Order order = findOrder(nr);
        if (OrderStatus.SHIPPED.equals(order.getStatus())) {
            throw new OrderAlreadyShippedException();
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepo.edit(order);
    }

    @Override
    public Order findOrder(Long nr) throws OrderNotFoundException {
        List<Order> orders = orderRepo.findByNr(nr);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotFoundException();
        }
        return orders.get(0);
    }

    @Override
    public Order placeOrder(Customer customer, List<OrderItem> items) throws PaymentFailedException {
        Order order = new Order();
        order.setCustomer(customer);
        order.setItems(new HashSet<>(items));
        copyCustomerInfos(customer, order);
        validateOrderPlacement(order);
        orderRepo.create(order);

        return order;
    }

    private void copyCustomerInfos(Customer customer, Order order) {
        order.setCreditCard(customer.getCreditCard());
        order.setAddress(customer.getAddress());
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

        //TODO isCreditCardInvalid --> what to check here????
    }

    private boolean isPaymentLimitExceeded(Order order) {
        return order.getAmount().compareTo(PAYMENT_LIMIT) > 0;
    }

    private boolean isCreditCardExpired(LocalDate now, CreditCard creditCard) {
        return creditCard.getExpirationYear() < now.getYear() && creditCard.getExpirationMonth() < now.getMonthValue();
    }

    @Override
    public List<OrderInfo> searchOrders(Customer customer, Integer year) {
        return orderRepo.findByCustomerAndYear(customer.getNr(), year);
    }

}
