package ch.bfh.eadj.entity;


import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "T_ORDERITEM")
@Entity
public class OrderItem extends  BaseEntity {

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

