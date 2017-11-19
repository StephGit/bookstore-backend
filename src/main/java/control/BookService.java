package control;

import entity.Book;

import java.util.List;

public interface BookService {

    public void addBook(Book book);
    public Book get(String isbn);
    public List<Book> find(String ...keyword);
    public void removeBook(Book book);
    public void updateBook(Book book);

}
