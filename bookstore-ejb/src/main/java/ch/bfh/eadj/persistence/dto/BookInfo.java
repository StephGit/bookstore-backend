package ch.bfh.eadj.persistence.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BookInfo implements Serializable {

    private final String isbn;
    private final String authors;
    private final String title;
    private final BigDecimal price;

    public BookInfo(String isbn, String authors, String title, BigDecimal price) {
        this.isbn = isbn;
        this.authors = authors;
        this.title = title;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
