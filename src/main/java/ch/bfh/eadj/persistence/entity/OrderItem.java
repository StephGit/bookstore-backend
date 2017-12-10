package ch.bfh.eadj.persistence.entity;


import javax.persistence.*;

@Table(name = "T_ORDERITEM")
@Entity
public class OrderItem extends  BaseEntity {

    @Column(nullable = false)
    private Integer quantity;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Book book;

    public Book getBook() { return book; }

    public Integer getQuantity() {
        return quantity;
    }

    public void setBook(Book book) { this.book = book; }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

