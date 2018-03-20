package ch.bfh.eadj.persistence.entity;

import ch.bfh.eadj.persistence.enumeration.BookBinding;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Table(name = "T_BOOK")
@Entity
@NamedQueries({
        @NamedQuery(name = Book.FIND_BY_ISBN_QUERY.QUERY_NAME, query = Book.FIND_BY_ISBN_QUERY.QUERY_STRING)
})
public class Book extends BaseEntity {

    public static class FIND_BY_ISBN_QUERY {
        public static final String QUERY_NAME = "Book.findByISBN";
        public static final String PARAM_ISBN = "isbn";
        public static final String QUERY_STRING = "select b from Book b where b.isbn = :isbn";
    }

    @NotNull
    @Column(nullable = false)
    private String isbn;

    private String authors;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    private String publisher;

    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    private BookBinding binding;

    private Integer numberOfPages;

    @Column(length = 5000)
    private String description;

    private String imageUrl;

    public Book() {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!isbn.equals(book.isbn)) return false;
        if (authors != null ? !authors.equals(book.authors) : book.authors != null) return false;
        if (!title.equals(book.title)) return false;
        if (price != null ? !price.equals(book.price) : book.price != null) return false;
        if (publisher != null ? !publisher.equals(book.publisher) : book.publisher != null) return false;
        if (publicationYear != null ? !publicationYear.equals(book.publicationYear) : book.publicationYear != null)
            return false;
        if (binding != book.binding) return false;
        if (numberOfPages != null ? !numberOfPages.equals(book.numberOfPages) : book.numberOfPages != null)
            return false;
        if (description != null ? !description.equals(book.description) : book.description != null) return false;
        return imageUrl != null ? imageUrl.equals(book.imageUrl) : book.imageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = isbn.hashCode();
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (publicationYear != null ? publicationYear.hashCode() : 0);
        result = 31 * result + (binding != null ? binding.hashCode() : 0);
        result = 31 * result + (numberOfPages != null ? numberOfPages.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }
}
