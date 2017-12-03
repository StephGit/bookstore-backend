package ch.bfh.eadj.control.catalog;

import ch.bfh.eadj.control.exception.BookAlreadyExistsException;
import ch.bfh.eadj.control.exception.BookNotFoundException;
import ch.bfh.eadj.dto.BookInfo;
import ch.bfh.eadj.entity.Book;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface CatalogServiceRemote {

    Book findBook(String isbn)
            throws BookNotFoundException;

    void addBook(Book book)
            throws BookAlreadyExistsException;


    List<BookInfo> searchBooks(String keywords);


    void updateBook(Book book)
            throws BookNotFoundException;

}
