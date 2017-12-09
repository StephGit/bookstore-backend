package ch.bfh.eadj.control.order;

import ch.bfh.eadj.control.exception.OrderAlreadyShippedException;
import ch.bfh.eadj.control.exception.OrderNotFoundException;
import ch.bfh.eadj.control.exception.PaymentFailedException;
import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.entity.Customer;
import ch.bfh.eadj.entity.Order;
import ch.bfh.eadj.entity.OrderItem;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface OrderServiceRemote {

    public void cancelOrder(Long nr) throws OrderNotFoundException, OrderAlreadyShippedException;

    public Order findOrder(Long nr) throws OrderNotFoundException;

    public Order placeOrder(Customer customer, List<OrderItem> items) throws PaymentFailedException;

    public List<OrderInfo> searchOrders(Customer customer, Integer year);

}
