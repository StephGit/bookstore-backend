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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AmazonCatalog {
    public static final String MEDIUM = "Medium";
    public static final String SEARCH_INDEX = "Books";
    private static final String ASSOCIATE_TAG = "test0953-20";

    private static final Logger logger = Logger.getLogger(AmazonCatalog.class.getName());

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType port;


    public Book findBook(String isbn) throws BookNotFoundException {
        ItemLookup lookup = new ItemLookup();
        lookup.setAssociateTag(ASSOCIATE_TAG);
        ItemLookupRequest request = new ItemLookupRequest();
        request.getItemId().add(isbn);
        request.getResponseGroup().add(MEDIUM);
        request.setTruncateReviewsAt(BigInteger.valueOf(254)); // doesnt work???
        lookup.setShared(request);
        ItemLookupResponse itemLookupResponse = this.port.itemLookup(lookup);

        Book book = extractResult(validateFindResult(itemLookupResponse.getItems()));
        logger.info("result from book lookup with isbn: " + isbn + " : " + book.toString());
        return book;
    }


    private Book extractResult(Item item) {
        ItemAttributes itemAttributes = item.getItemAttributes();
        Book book = new Book();
        book.setIsbn(itemAttributes.getISBN());
        book.setTitle(itemAttributes.getTitle());
        book.setAuthors(itemAttributes.getAuthor().toString());
        book.setBinding(BookBinding.getBinding(itemAttributes.getBinding()));
        book.setPrice(getPrice(item.getOfferSummary().getLowestNewPrice()));
        book.setDescription(item.getEditorialReviews().getEditorialReview().get(0).getContent());
        book.setPublisher(itemAttributes.getPublisher());
        book.setPublicationYear(validatePublicationDate(itemAttributes.getPublicationDate()));
        book.setImageUrl(item.getMediumImage().getURL());
        book.setNumberOfPages(itemAttributes.getNumberOfPages().intValue());
        return book;
    }

    private Item validateFindResult(List<Items> itemsList) throws BookNotFoundException {
        if (itemsList == null || itemsList.isEmpty() || itemsList.get(0).getItem().isEmpty()) {
            if (!itemsList.get(0).getRequest().getErrors().getError().isEmpty()) {
                itemsList.get(0).getRequest().getErrors().getError().forEach(error ->
                    logger.warning(error.getCode() + ":" + error.getMessage())
                );
            }
            throw new BookNotFoundException();
        } else {
            for (Items items : itemsList) {
                Item item = items.getItem().get(0);
                if (item.getItemAttributes().getISBN() != null) {
                    return item;
                }
            }
            throw new BookNotFoundException();
        }
    }

    private Integer validatePublicationDate(String publicationDate) {
        if (publicationDate.length() == 4) {
            return Integer.parseInt(publicationDate);
        } else {
            return LocalDate.parse(publicationDate).getYear();
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
        List<BookInfo> results = new ArrayList<>();
        extractSearchResult(itemSearchResponse, results);
        int totalPages = itemSearchResponse.getItems().get(0).getTotalPages().intValue();
        int pages = totalPages > 10 ? 10 : totalPages;
        logger.info("result from book search with keywords: " + keywords + " : " + results.toString());
        return results;

    }

    private void extractSearchResult(ItemSearchResponse response, List<BookInfo> results) {
        List<Item> item = response.getItems().get(0).getItem();
        for (Item i : item) {
            ItemAttributes attributes = i.getItemAttributes();
            if (isResultInvalid(attributes)) {
                continue;
            }
            BookInfo b = new BookInfo(attributes.getISBN(), attributes.getAuthor().toString(), attributes.getTitle(),
                    getPrice(attributes.getListPrice()));
            results.add(b);

        }
    }

    private BigDecimal getPrice(Price price) {
        if ((price != null) && (price.getAmount() != null)) {
            return new BigDecimal(price.getAmount()).divide(new BigDecimal(100));
        } else {
            return null;
        }

    }

    private boolean isResultInvalid(ItemAttributes attributes) {
        return attributes.getISBN() == null || attributes.getISBN().isEmpty() ||
                attributes.getTitle() == null || attributes.getTitle().isEmpty()
                || attributes.getListPrice() == null || attributes.getListPrice().getAmount() == null;
    }
}
