package ch.bfh.eadj.persistence.dto;

import ch.bfh.eadj.persistence.enumeration.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo implements Serializable {

    private Long nr;
    private final Date date;
    private final BigDecimal amount;
    private final OrderStatus status;

    public OrderInfo(Long nr, Date date, BigDecimal amount, OrderStatus status) {
        this.nr = nr;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public Long getNr() {
        return nr;
    }

    public void setNr(Long nr) {
        this.nr = nr;
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
}
