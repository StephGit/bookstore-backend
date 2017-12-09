package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface CatalogServiceRemote {

    Book findBook(String isbn)
            throws BookNotFoundException;

    void addBook(Book book)
            throws BookAlreadyExistsException;

    void removeBook(Long id);

    List<BookInfo> searchBooks(String keywords);


    void updateBook(Book book)
            throws BookNotFoundException;

}
