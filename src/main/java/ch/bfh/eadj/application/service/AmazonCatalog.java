package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.enumeration.BookBinding;
import org.contacts.soap.*;

import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AmazonCatalog {
    public static final String MEDIUM = "Medium";
    public static final String  SEARCH_INDEX = "Books";
    private static final String ASSOCIATE_TAG = "test";

    private static final Logger logger = Logger.getLogger(AmazonCatalog.class.getName());

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType port;


    public Book findBook(String isbn) throws BookNotFoundException {
        ItemLookup lookup = new ItemLookup();
        lookup.setAssociateTag(ASSOCIATE_TAG);
        lookup.setShared(new ItemLookupRequest());
        ItemLookupRequest request = lookup.getShared();
        request.getItemId().add(isbn);
//        request.setSearchIndex(SEARCH_INDEX); When IdType equals ASIN, SearchIndex cannot be present.
        request.getResponseGroup().add(MEDIUM);
        //TODO productGroup
        ItemLookupResponse itemLookupResponse = this.port.itemLookup(lookup);

        validateResult(itemLookupResponse.getItems());
        Book book = extractResult(itemLookupResponse.getItems());
        logger.info(book.toString());
        return book;
    }

    private Book extractResult(List<Items> items) {

        //TODO wenn mehrere bücher zurück kommen das erst beste mit der ISBN --> siehe fischli testcase

        Item item = items.get(0).getItem().get(0);
        ItemAttributes itemAttributes = item.getItemAttributes();
        Book book = new Book();
        book.setIsbn(itemAttributes.getISBN());
        book.setTitle(itemAttributes.getTitle());
        book.setAuthors(itemAttributes.getManufacturer());
        book.setBinding(BookBinding.HARDCOVER); //TODO compare string and set enum according to response
        book.setPrice(new BigDecimal(itemAttributes.getListPrice().getAmount()));
        book.setDescription(item.getEditorialReviews().getEditorialReview().get(0).getContent()); // TODO where to find description in response?
        book.setPublisher(itemAttributes.getPublisher());
//        book.setPublicationYear(itemAttributes.getPublicationDate());  TODO extract year from date
        book.setImageUrl(item.getMediumImage().getURL());
        book.setNumberOfPages(itemAttributes.getNumberOfPages().intValue());
        return book;

    }

    private void validateResult(List<Items> items) throws BookNotFoundException {
        //TODO extract and log Errors properly.. i.e missing associate tag
        //TODO test for each error
        if (items == null || items.isEmpty() || items.get(0).getItem().isEmpty()) {
            throw new BookNotFoundException();
        }

    }


    public List<BookInfo> searchBooks(String keywords) {

        ItemSearch search = new ItemSearch();
        ItemSearchRequest shared = search.getShared();

        shared.setSearchIndex(SEARCH_INDEX);
        shared.setKeywords(keywords);

        ItemSearchResponse itemSearchResponse = port.itemSearch(search);

        //TODO extract response
        System.out.println(itemSearchResponse);
        return null;

    }



}
