package entity;

import dto.BookInfo;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NamedQueries({
        @NamedQuery(name = Book.GET_BY_ISBN,
                query = "select b from Book b where b.isbn = :isbn"),
        @NamedQuery(name = Book.FIND_BY_KEYWORD, query = "select b from Book b where (b.title in :keywords) or (b.authors in :keywords) or (b.publisher in :keywords)")
})
//TODO muss id zusätzlich in DTO?¨Oder evtl. isbn auf column="nr" mappen
@SqlResultSetMapping(name = "BookInfo",
        classes = {
                @ConstructorResult(targetClass = BookInfo.class,
                        columns = {
                                @ColumnResult(name = "isbn"),
                                @ColumnResult(name = "authors"),
                                @ColumnResult(name = "title"),
                                @ColumnResult(name = "price")}
                )
        })
public class Book extends BaseEntity {

    private final static String GET_BY_ISBN = "Book.findByISBN";

    private final static String FIND_BY_KEYWORD = "Book.findByKeyword";

    private String isbn;

    private String authors;

    private String title;

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
