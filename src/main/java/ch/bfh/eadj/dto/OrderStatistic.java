package ch.bfh.eadj.dto;

import java.math.BigDecimal;

public class OrderStatistic {

    private Integer positionsCount;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;


// TODO Year & customerinfo, id, name prename
    public OrderStatistic(Integer positionsCount, BigDecimal totalAmount, BigDecimal averageAmount) {
        this.positionsCount = positionsCount;
        this.totalAmount = totalAmount;
        this.averageAmount = averageAmount;
    }


    public Integer getPositionsCount() {
        return positionsCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }
}
