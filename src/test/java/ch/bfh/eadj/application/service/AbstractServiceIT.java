package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.BookNotFoundException;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.OrderItem;
import ch.bfh.eadj.persistence.enumeration.CreditCardType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AbstractServiceIT {

    protected Customer createCustomer() {
        Customer cust  = new Customer();
        cust.setEmail("hans@dampf.ch");
        cust.setFirstName("Hans");
        cust.setLastName("Dampf");
        cust.setCreditCard(createCreditCard());
        return cust;
    }

    protected CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationMonth(8);
        creditCard.setExpirationYear(2019);
        creditCard.setNumber("232232221231211112");
        creditCard.setType(CreditCardType.MASTERCARD);
        return creditCard;
    }

    protected Book createBook() {
        Book b = new Book();
        b.setTitle("test");
        b.setIsbn("12345");
        b.setAuthors("max muster");
        b.setPrice(new BigDecimal("100.14"));
        return b;
    }

    protected List<OrderItem> createOrderItems(int items, Book book) throws BookNotFoundException {
        List<OrderItem> orderItems = new ArrayList<>();
        for ( int i = 0; i < items; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setQuantity(1);
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
