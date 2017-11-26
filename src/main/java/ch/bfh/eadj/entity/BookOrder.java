package ch.bfh.eadj.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "T_BOOKORDER")
@NamedQueries({
        @NamedQuery(name = BookOrder.FIND_BY_NR_QUERY.QUERY_NAME, query = BookOrder.FIND_BY_NR_QUERY.QUERY_STRING),
        @NamedQuery(name = BookOrder.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_NAME, query = BookOrder.FIND_BY_CUSTOMER_AND_YEAR_QUERY.QUERY_STRING),
        @NamedQuery(name = BookOrder.STATISTIC_BY_YEAR_QUERY.QUERY_NAME, query = BookOrder.STATISTIC_BY_YEAR_QUERY.QUERY_STRING)
})
public class BookOrder extends BaseEntity implements Serializable {


    public static final String PARAM_NR = "nr";
    public static final String PARAM_YEAR = "year";

    public static class FIND_BY_NR_QUERY {
        public static final String QUERY_NAME = "BookOrder.findById";
        public static final String QUERY_STRING = "select new ch.bfh.eadj.dto.OrderInfo(o.nr, o.date, o.amount, o.status) from BookOrder o where o.nr = :nr";
    }

    public static class FIND_BY_CUSTOMER_AND_YEAR_QUERY {
        public static final String QUERY_NAME = "BookOrder.findByCustomerAndYear";
        public static final String QUERY_STRING = "select new ch.bfh.eadj.dto.OrderInfo(o.nr, o.date, o.amount, o.status) from BookOrder o" +
                " join o.customer c where c.nr = :nr and extract(YEAR from o.date) = :year";
    }
    /*
    Es werden nur Bestellungen berücksichtigen, welche nicht den Status.CANCELED haben. Ansonsten ist Statistik nicht aussagekräftig.
     */
    public static class STATISTIC_BY_YEAR_QUERY {
        public static final String QUERY_NAME = "BookOrder.statisticByYear";
        public static final String QUERY_STRING = "select new ch.bfh.eadj.dto.OrderStatisticInfo(sum(o.amount), count(oi), (sum(o.amount)/count(oi)), c.nr, c.firstName, c.lastName ) " +
                "from BookOrder o join o.customer c join o.orderItems oi where EXTRACT(YEAR from o.date) = :year and o.status not like ('CANCELED')  group by c.nr";
    }

    private static final long serialVersionUID = 1L;

    @Column(name = "ORDER_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal amount;

    /*
    Wenn ein BookOrder persistiert wird sollen auch alle dazugehörigen OrderItems kaskadierend persisitert werden
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /*
    Kaskadierung:
    OrderItems sind Komposition zu BookOrder. Klassischerweise wird für Kompositionen CascadeType.ALL verwendet (eine Entity kann nicht ohne ein anderes bestehen)
    Weisenkinder sollen deshalb gelöscht werden sobald die Beziehung entfernt wird. Referenzielle Integrität wird so eingehalten.

    Beziehung:
    Unidirektionale OneToMany Beziehung benötigt das Angeben der JoinColumn (lebt auf OrderItem als owining Seite)
    Lässt man das weg entsteht eine Zwischentabelle welche Performanceeinbussen zur Folge hat

    Fetch-Typ:
    Eager nicht zwingend, da evtl. nur Orderinfos von Interesse sind ohne einzelne Positionen.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_NR") //OrderItem besitzt ORDER_ID FK Column
    private Set<OrderItem> orderItems = new HashSet<>();

    /*
    Kaskadierung:
    Wird ein BookOrder mit einem Customer zusammen angelegt soll kaskadiert persisiert werden.

    Fetch-Typ:
    Lazy, da die Adresse und Kreditkarte bereits auf dem BookOrder vorhanden ist und sonstige Kundeninfos nicht immer benötigt werden.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    private Customer customer;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;


    public Set<OrderItem> getOrderItems() {
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

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);

    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);

    }
}
