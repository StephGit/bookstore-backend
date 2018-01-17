package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import org.contacts.soap.*;

import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import java.util.List;

@Stateless
public class AmazonCatalog {
    public static final String MEDIUM = "Medium";
    public static final String  SEARCH_INDEX = "Books";

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType port;


    public Book findBook(String isbn) throws BookNotFoundException {
        ItemLookup lookup = new ItemLookup();
        ItemLookupRequest request = lookup.getShared();
        request.setIdType(isbn);
        request.setSearchIndex(SEARCH_INDEX);
        request.getResponseGroup().add(MEDIUM);
        //TODO productGroup
        ItemLookupResponse itemLookupResponse = this.port.itemLookup(lookup);
        //TODO extract response
        System.out.println(itemLookupResponse);
        return null;


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
