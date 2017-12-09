package ch.bfh.eadj.application.service;

import ch.bfh.eadj.persistence.repository.BookRepository;
import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CatalogService implements CatalogServiceRemote{


    private static final Logger logger = Logger.getLogger(CatalogService.class.getName());


    @Inject
    private BookRepository bookRepo;

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        List<Book> bookByIsbn = bookRepo.findByIsbn(isbn);
        if (bookByIsbn == null || bookByIsbn.isEmpty()) {
            throw new BookNotFoundException();
        }
        return bookByIsbn.get(0);
    }

    @Override
    public void addBook(Book book) throws BookAlreadyExistsException {
        logger.warning("entering add method");
        List<Book> books = bookRepo.findByIsbn(book.getIsbn());
        if (books != null && !books.isEmpty()) {
            throw new BookAlreadyExistsException();
        }
        bookRepo.create(book);
    }

    @Override
    public void removeBook(Long id) {
        bookRepo.deleteBook(id);
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        if (keywords != null && keywords.length() > 0) {
            String[] splited = keywords.split("\\s+");
            return bookRepo.findByKeywords(Arrays.asList(splited));
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
