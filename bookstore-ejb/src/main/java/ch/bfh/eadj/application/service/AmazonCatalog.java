package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.enumeration.BookBinding;
import org.contacts.soap.*;

import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AmazonCatalog {
    public static final String MEDIUM = "Medium";
    public static final String SEARCH_INDEX = "Books";
    private static final String ASSOCIATE_TAG = "test";

    private static final Logger logger = Logger.getLogger(AmazonCatalog.class.getName());

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType port;


    public Book findBook(String isbn) throws BookNotFoundException {
        ItemLookup lookup = new ItemLookup();
        lookup.setAssociateTag(ASSOCIATE_TAG);
        ItemLookupRequest request = new ItemLookupRequest();
        request.getItemId().add(isbn);
//        request.setSearchIndex(SEARCH_INDEX); When IdType equals ASIN, SearchIndex cannot be present.
        request.getResponseGroup().add(MEDIUM);
        lookup.setShared(request);
        ItemLookupResponse itemLookupResponse = this.port.itemLookup(lookup);

        validateFindResult(itemLookupResponse.getItems());
        Book book = extractResult(itemLookupResponse.getItems());
        logger.info("result from book lookup with isbn: " + isbn + " : " + book.toString());
        return book;
    }

    private Book extractResult(List<Items> items) {

        //TODO wenn mehrere bücher zurück kommen das erst beste mit der ISBN --> siehe fischli testcase

        Item item = items.get(0).getItem().get(0);
        ItemAttributes itemAttributes = item.getItemAttributes();
        Book book = new Book();
        book.setIsbn(itemAttributes.getISBN());
        book.setTitle(itemAttributes.getTitle());
        book.setAuthors(itemAttributes.getAuthor().toString());
        book.setBinding(BookBinding.getBinding(itemAttributes.getBinding()));
        book.setPrice(new BigDecimal(item.getOfferSummary().getLowestNewPrice().getAmount()).divide(new BigDecimal(100)));
        book.setDescription(item.getEditorialReviews().getEditorialReview().get(0).getContent());
        book.setPublisher(itemAttributes.getPublisher());

        //TODO test --- doesnt work
        //book.setPublicationYear(LocalDate.parse(itemAttributes.getPublicationDate()).getYear());
        book.setImageUrl(item.getMediumImage().getURL());
        book.setNumberOfPages(itemAttributes.getNumberOfPages().intValue());
        return book;

    }

    private void validateFindResult(List<Items> items) throws BookNotFoundException {
        //TODO extract and log Errors properly.. i.e missing associate tag
        //TODO test for each error
        if (items == null || items.isEmpty() || items.get(0).getItem().isEmpty()) {
            throw new BookNotFoundException();
        }

    }


    public List<BookInfo> searchBooks(String keywords) {

        ItemSearch search = new ItemSearch();
        search.setAssociateTag(ASSOCIATE_TAG);
        ItemSearchRequest shared = new ItemSearchRequest();
        shared.setItemPage(BigInteger.ONE);
        shared.setSearchIndex(SEARCH_INDEX);
        shared.setKeywords(keywords);
        search.setShared(shared);
        shared.getResponseGroup().add("ItemIds");
        shared.getResponseGroup().add("ItemAttributes");

        ItemSearchResponse itemSearchResponse = port.itemSearch(search);
        //TODO validate
        List<BookInfo> results = new ArrayList<>();
        extractSearchResult(itemSearchResponse, results);
        int totalPages = itemSearchResponse.getItems().get(0).getTotalPages().intValue();
        int pages = totalPages > 10 ? 10 : totalPages;
        for (int i = 2; i <= pages; i++) {
            // request, add to list, next;
            shared.setItemPage(BigInteger.valueOf(i));

            //leider sind batch requests nicht möglich bzw nur mit zwei searches pro request
            ItemSearchResponse response = port.itemSearch(search);
            extractSearchResult(response, results);
        }
        logger.info("result from book search with keywords: " + keywords + " : " + results.toString());
        return results;

    }

    private void extractSearchResult(ItemSearchResponse response, List<BookInfo> results) {
        List<Item> item = response.getItems().get(0).getItem();
        for (Item i : item) {
            ItemAttributes attributes = i.getItemAttributes();

            //TODO nur bücher mit isbn, titel, blabla -> validieren bzw herausfiltern!
            if (isResultInvalid(attributes)) {
                continue;
            }
            BookInfo b = new BookInfo(attributes.getISBN(), attributes.getAuthor().toString(), attributes.getTitle(), BigDecimal.ONE); //TODO get correct price
            results.add(b);

        }
    }

    private boolean isResultInvalid(ItemAttributes attributes) {
        return attributes.getISBN() == null || attributes.getISBN().isEmpty() || attributes.getTitle() == null || attributes.getTitle().isEmpty();
    }


}
