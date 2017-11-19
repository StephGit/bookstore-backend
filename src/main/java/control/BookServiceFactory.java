package control;

public class BookServiceFactory {

    public BookService createBookService() {
        return new BookServiceImpl();
    }
}
