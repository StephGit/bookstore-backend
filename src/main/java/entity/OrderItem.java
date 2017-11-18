package entity;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem extends  BaseEntity{

    private Integer quantity;

    @ManyToOne
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
