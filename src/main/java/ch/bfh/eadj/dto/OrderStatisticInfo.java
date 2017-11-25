package ch.bfh.eadj.dto;

import java.math.BigDecimal;

public class OrderStatisticInfo {

    private Long positionsCount;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;
    private Long nr;
    private String lastName;
    private String firstName;

    public OrderStatisticInfo(BigDecimal totalAmount, Long positionsCount, BigDecimal averageAmount, Long nr, String lastName, String firstName) {
        this.positionsCount = positionsCount;
        this.totalAmount = totalAmount;
        this.averageAmount = averageAmount;
        this.nr = nr;
        this.lastName = lastName;
        this.firstName = firstName;
    }


    public Long getPositionsCount() { return positionsCount; }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public Long getNr() { return nr; }

    public String getLastName() { return lastName; }

    public String getFirstName() { return firstName; }

}
