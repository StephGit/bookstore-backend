package entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem extends  BaseEntity{

    private Integer quantity;

    /*
    Dieser Navigationsweg ist eher selten. Deshalb kann der Order zum OrderItem Lazy geladen werden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    public Integer getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
