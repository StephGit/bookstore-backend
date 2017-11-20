package entity;

import dto.OrderInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "T_ORDER")
@SqlResultSetMapping(name = "OrderInfo",
        classes = {
                @ConstructorResult(targetClass = OrderInfo.class,
                        columns = {
                                @ColumnResult(name = "nr"),
                                @ColumnResult(name = "date"),
                                @ColumnResult(name = "amount"),
                                @ColumnResult(name = "status")}
                )
        })
public class Order extends BaseEntity implements Serializable {
//TODO achtung order ist ein oracle keyword

    public static class FIND_BY_ID_QUERY {
        public static final String QUERY_NAME = "findbyId";
        public static final String QUERY_STRING = "findbyId";
    }

    private static final long serialVersionUID = 1L;

    private Date date;

    private BigDecimal amount;

    /*
    Wenn ein Order persistiert wird sollen auch alle dazugehörigen OrderItems kaskadierend persisitert werden
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    private Customer customer;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;


    public void addOrderItem(OrderItem item) {
        orderItems.add(item);

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
