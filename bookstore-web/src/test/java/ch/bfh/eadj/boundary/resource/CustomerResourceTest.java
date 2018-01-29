package ch.bfh.eadj.boundary.resource;

import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.enumeration.Country;
import ch.bfh.eadj.persistence.enumeration.CreditCardType;
import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CustomerResourceTest {

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/bookstore/api/customers";
    }

    @Test
    public void authenticateCustomer() {
    }

    @Test
    public void changePassword() {
    }

    @Test
    public void findCustomer() {
        //

    }

    @Test
    public void shouldRegisterCustomer() {
    }

    @Test
    public void searchCustomers() {
    }

    @Test
    public void updateCustomer() {
    }

    private Customer createCustomer() {
        Customer cust  = new Customer();
        cust.setEmail("hans1@dampf.ch");
        cust.setFirstName("Hansi");
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
        creditCard.setExpirationYear(LocalDate.now().getYear()+2);
        creditCard.setNumber("2322322212312111");
        creditCard.setType(CreditCardType.MASTERCARD);
        return creditCard;
    }
}