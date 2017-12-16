package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.*;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.entity.OrderItem;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface OrderServiceRemote {

    void cancelOrder(Long nr) throws OrderNotFoundException, OrderAlreadyShippedException, OrderProcessingException, OrderAlreadyCanceledException;

    Order findOrder(Long nr) throws OrderNotFoundException;

    Order placeOrder(Customer customer, List<OrderItem> items) throws PaymentFailedException, OrderProcessingException;

    List<OrderInfo> searchOrders(Customer customer, Integer year);

    void removeOrder(Order order);
}
