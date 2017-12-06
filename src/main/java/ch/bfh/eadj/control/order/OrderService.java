package ch.bfh.eadj.control.order;

import ch.bfh.eadj.dto.OrderInfo;
import ch.bfh.eadj.entity.Customer;
import ch.bfh.eadj.entity.Order;
import ch.bfh.eadj.entity.OrderItem;
import ch.bfh.eadj.entity.OrderStatus;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;

@Stateless
public class OrderService implements OrderServiceRemote {

    @Inject
    OrderRepository orderRepo;

    @Override
    public void cancelOrder(Long nr) {
        Order order = orderRepo.findByNr(nr);
        order.setStatus(OrderStatus.CANCELED);
        orderRepo.edit(order);
    }

    @Override
    public Order findOrder(Long nr) {
        return orderRepo.findByNr(nr);
    }

    @Override
    public Order placeOrder(Customer customer, List<OrderItem> items) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(new HashSet<>(items));
        orderRepo.create(order);
        return order;
    }

    @Override
    public List<OrderInfo> searchOrder(Customer customer, Integer year) {
        return orderRepo.findByCustomerAndYear(customer.getNr(), year);
    }

}
