package ch.bfh.eadj.persistence.dto;

import ch.bfh.eadj.persistence.enumeration.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo implements Serializable {

    private Long nr;
    private Date date;
    private BigDecimal amount;
    private OrderStatus status;

    public OrderInfo(Long nr, Date date, BigDecimal amount, OrderStatus status) {
        this.nr = nr;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }
    //Constructor for JSON-Parsing in REST-Interface
    public OrderInfo() {
    }

    public Long getNr() {
        return nr;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    //Setter for JSON-Parsing in REST-Interface
    public void setNr(Long nr) {
        this.nr = nr;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setDate(Date date) {
        this.date = date;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
