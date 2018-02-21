package ch.bfh.eadj.boundary.dto;

import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.OrderItem;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class SalesOrderDTO {

    private Long nr;
    private OrderStatus orderStatus;
    private Address address;
    private BigDecimal amount;
    private CreditCard creditCard;
    private Date orderDate;
    private Set<OrderItem> items;
    private Customer customer;

    public SalesOrderDTO() {}

    public SalesOrderDTO(Long nr, OrderStatus status, Address address, BigDecimal amount,
                         CreditCard creditCard, Date date, Set<OrderItem> items, Customer customer) {
    this.nr = nr;
    this.orderStatus = status;
    this.address = address;
    this.amount = amount;
    this.creditCard = creditCard;
    this.orderDate = date;
    this.items = items;
    this.customer = customer;
    }

    public Long getNr() {
        return nr;
    }

    public void setNr(Long nr) {
        this.nr = nr;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
