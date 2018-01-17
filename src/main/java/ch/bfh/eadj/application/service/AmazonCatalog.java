package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.dto.BookInfo;
import ch.bfh.eadj.persistence.entity.Book;
import org.contacts.soap.AWSECommerceService;
import org.contacts.soap.AWSECommerceServicePortType;

import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Stateless
public class AmazonCatalog {

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType port;


    public Book findBook(String isbn) throws BookNotFoundException {
        //port.itemLookup()


    }


    public List<BookInfo> searchBooks(String keywords) {
        //port.itemSearch();
    }



}
