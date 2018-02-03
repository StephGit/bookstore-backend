package ch.bfh.eadj.boundary.dto;

public class OrderItemDTO {

    private String isbn;
    private Integer quantity;


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
