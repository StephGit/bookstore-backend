package dto;

import entity.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo {

    private Long id;
    private Date date;
    private BigDecimal amount;
    private OrderStatus status;

    public OrderInfo(Long id, Date date, BigDecimal amount, OrderStatus status) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
