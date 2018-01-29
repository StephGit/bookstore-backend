package ch.bfh.eadj.boundary.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private Long customerNr;
    private List<OrderItemDTO> items = new ArrayList<>();


    public Long getCustomerNr() {
        return customerNr;
    }

    public void setCustomerNr(Long customerNr) {
        this.customerNr = customerNr;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

}
