package ch.bfh.eadj.application.service;

import ch.bfh.eadj.persistence.entity.*;
import ch.bfh.eadj.persistence.enumeration.Country;
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
        cust.setAddress(createAdddress());
        return cust;
    }

    private Address createAdddress() {
       return new Address("Bahnstrasse", "Burgdorf", "3400", Country.CH);
    }

    private CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationMonth(8);
        creditCard.setExpirationYear(2019);
        creditCard.setNumber("2322322212312111");
        creditCard.setType(CreditCardType.MASTERCARD);
        return creditCard;
    }

    Book createBook(String title, String isbn, String author) {
        Book b = new Book();
        b.setTitle(title);
        b.setIsbn(isbn);
        b.setAuthors(author);
        b.setPrice(new BigDecimal("100.14"));
        return b;
    }

    List<OrderItem> createOrderItems(int items, Book book) {
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
