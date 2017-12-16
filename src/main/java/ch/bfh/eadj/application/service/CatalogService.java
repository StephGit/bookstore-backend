package ch.bfh.eadj.application.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ch.bfh.eadj.application.exception.BookAlreadyExistsException;
import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.repository.BookRepository;

@Stateless
public class CatalogService implements CatalogServiceRemote{


    @EJB
    BookRepository bookRepo;

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
