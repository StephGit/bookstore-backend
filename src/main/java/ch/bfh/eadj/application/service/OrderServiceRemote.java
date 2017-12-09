package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.OrderAlreadyShippedException;
import ch.bfh.eadj.application.exception.OrderNotFoundException;
import ch.bfh.eadj.application.exception.PaymentFailedException;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.entity.OrderItem;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface OrderServiceRemote {

    void cancelOrder(Long nr) throws OrderNotFoundException, OrderAlreadyShippedException;

    Order findOrder(Long nr) throws OrderNotFoundException;

    Order placeOrder(Customer customer, List<OrderItem> items) throws PaymentFailedException;

    List<OrderInfo> searchOrders(Customer customer, Integer year);

}
