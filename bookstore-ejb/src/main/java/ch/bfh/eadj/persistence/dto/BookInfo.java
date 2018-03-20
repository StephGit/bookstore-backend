package ch.bfh.eadj.persistence.dto;

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
    //Constructor for JSON-Parsing in REST-Interface
    public BookInfo() {
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

    //Setter for JSON-Parsing in REST-Interface
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setAuthors(String authors) {
        this.authors = authors;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setTitle(String title) {
        this.title = title;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
