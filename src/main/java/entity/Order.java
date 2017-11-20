package entity;

import com.sun.xml.internal.bind.v2.TODO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_ORDER")
@NamedQueries({
        @NamedQuery(name = Order.FIND_BY_ID_QUERY.QUERY_NAME, query = Order.FIND_BY_ID_QUERY.QUERY_STRING),
        @NamedQuery(name = Order.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_NAME, query = Order.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_STRING),
        @NamedQuery(name = Order.SUM_BY_YEAR_QUERY.QUERY_NAME, query = Order.SUM_BY_YEAR_QUERY.QUERY_STRING)
})
public class Order extends BaseEntity implements Serializable {
//TODO achtung order ist ein oracle keyword

    public static class FIND_BY_ID_QUERY {
        public static final String QUERY_NAME = "Order.findById";
        public static final String QUERY_STRING = "select new dto.OrderInfo(o.id, o.date, o.amount, o.status) from Order o where o.id = :id";
    }

    public static class FIND_BY_CUSTOMER_AND_YEAR_QUERY {
        public static final String QUERY_NAME = "Order.findByCustomerAndYear";
        public static final String QUERY_STRING = "select new dto.OrderInfo(o.id, o.date, o.amount, o.status) from Order o" +
                " join o.customer c where c.id = :id and extract(YEAR from o.date) = :year";
    }

    public static class SUM_BY_YEAR_QUERY {
        public static final String QUERY_NAME = "Order.sumByYear";
        public static final String QUERY_STRING = "select new dto.OrderStatistic(count(oi), sum(o.amount), " +
                "avg(o.amount))" +
                "from entity.Order o join o.customer c join o.orderItems oi where EXTRACT(YEAR from o.date) = :year group by c";
    }

    private static final long serialVersionUID = 1L;

    private Date date;

    private BigDecimal amount;

    /*
    Wenn ein Order persistiert wird sollen auch alle dazugehörigen OrderItems kaskadierend persisitert werden
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany
    @JoinColumn(name = "ORDER_ID") //OrderItem besitzt ORDER_ID FK Column
    @OrderBy("createdAt DESC")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    private Customer customer;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;


    public void addOrderItem(OrderItem item) {
        orderItems.add(item);

    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
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
