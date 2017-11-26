package ch.bfh.eadj.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "T_ORDERITEM")
@Entity
public class OrderItem extends  BaseEntity {

    @Column(nullable = false)
    private Integer quantity;

    @OneToOne
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

