package ch.bfh.eadj.control.order;

import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.entity.Customer;
import ch.bfh.eadj.entity.Order;
import ch.bfh.eadj.entity.OrderItem;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface OrderServiceRemote {

    public void cancelOrder(Long nr);

    public Order findOrder(Long nr);

    public Order placeOrder(Customer customer, List<OrderItem> items);

    public List<OrderInfo> searchOrder(Customer customer, Integer year);

}
