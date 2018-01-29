package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.repository.BookRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Stateless(name = "CatalogService")
@LocalBean
public class CatalogService implements CatalogServiceRemote{


    @Inject
    BookRepository bookRepo;

    @Inject
    AmazonCatalog amazonCatalog;

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        Book bookByIsbn = amazonCatalog.findBook(isbn);
        if (bookByIsbn == null) {
            throw new BookNotFoundException();
        }
        return bookByIsbn;
    }

    public Book findBookFromDb(String isbn) throws BookNotFoundException {
        List<Book> bookByIsbn = bookRepo.findByIsbn(isbn);
        if (bookByIsbn == null || bookByIsbn.isEmpty()) {
            throw new BookNotFoundException();
        }
        return bookByIsbn.get(0);
    }


    @Override
    public void addBook(Book book) throws BookAlreadyExistsException {
        List<Book> books = bookRepo.findByIsbn(book.getIsbn());
        if (books != null && !books.isEmpty()) {
            throw new BookAlreadyExistsException();
        }
        bookRepo.create(book);
    }

    @Override
    public void removeBook(Book book) {

        bookRepo.deleteBook(book.getNr());
    }

    /*@Override
    public List<BookInfo> searchBooks(String keywords) {
        if (keywords != null && keywords.length() > 0) {
            String caseInsensitive = keywords.toLowerCase();
            String[] splited = caseInsensitive.split("\\s+");
            return bookRepo.findByKeywords(Arrays.asList(splited));
        } else {
            return Collections.emptyList();
        }
    }*/

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        if (keywords != null && keywords.length() > 0) {
            String caseInsensitive = keywords.toLowerCase(); //TODO still needed?
            return amazonCatalog.searchBooks(keywords);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void updateBook(Book book) throws BookNotFoundException {
        List<Book> books = bookRepo.findByIsbn(book.getIsbn());
        if (books == null || books.isEmpty()) {
            throw new BookNotFoundException();
        }
        bookRepo.edit(book);
    }
}
