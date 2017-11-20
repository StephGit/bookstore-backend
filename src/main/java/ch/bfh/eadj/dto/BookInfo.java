package ch.bfh.eadj.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BookInfo implements Serializable {

    private String isbn;
    private String authors;
    private String title;
    private BigDecimal price;

    public BookInfo(String isbn, String authors, String title, BigDecimal price) {
        this.isbn = isbn;
        this.authors = authors;
        this.title = title;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
