package ch.bfh.eadj.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "T_BOOK")
@Entity
@NamedQueries({
        @NamedQuery(name = Book.FIND_BY_ISBN_QUERY.QUERY_NAME, query = Book.FIND_BY_ISBN_QUERY.QUERY_STRING)
})
public class Book extends BaseEntity {

    public static class FIND_BY_ISBN_QUERY {
        public static final String QUERY_NAME = "Book.findByISBN";
        public static final String QUERY_STRING = "select new ch.bfh.eadj.dto.BookInfo(b.isbn, b.authors, b.title, b.price) from Book b where b.isbn = :isbn";
    }

    @Column(nullable = false)
    private String isbn;

    private String authors;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal price;

    private String publisher;

    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    private BookBinding binding;

    private Integer numberOfPages;

    private String description;

    private String imageUrl;

    public Book() {}

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public BookBinding getBinding() {
        return binding;
    }

    public void setBinding(BookBinding binding) {
        this.binding = binding;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
