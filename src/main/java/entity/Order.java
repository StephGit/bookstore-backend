package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                " join o.customer c where c.id = :id and extract(year, o.date) = :year";
    }

    public static class SUM_BY_YEAR_QUERY {
        public static final String QUERY_NAME = "Order.sumByYear";
        public static final String QUERY_STRING = "select sum(o.amount), count(o.orderItems), " +
                "avg(o.amount) as average), " +
                "new dto.CustomerInfo(c.id, c.firstName, c.lastName, c.email) " +
                "from Order o join o.customer c where extract(year, o.date) = :year group by c";
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

    @OneToMany
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
