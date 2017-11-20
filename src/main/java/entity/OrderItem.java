package entity;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class OrderItem extends  BaseEntity {

    private Integer quantity;

    @OneToOne
    private Book book;

    @ManyToOne
    private Order order;

    public Book getBook() { return book; }

    public Integer getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setBook(Book book) { this.book = book; }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

