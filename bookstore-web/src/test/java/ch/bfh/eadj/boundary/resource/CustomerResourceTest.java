package ch.bfh.eadj.boundary.resource;

import ch.bfh.eadj.boundary.dto.CustomerDTO;
import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.enumeration.Country;
import ch.bfh.eadj.persistence.enumeration.CreditCardType;
import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.time.LocalDate;

import static com.jayway.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CustomerResourceTest {

    private String password = "asdfkjl";

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
    public void shouldFindCustomer() {
        CustomerDTO customer = createCustomerDTO();
        customer.setEmail("Bruno3@Gans.ch");

        given().
                accept(TEXT_PLAIN)
                .contentType(APPLICATION_JSON)
                .header("password", password)
                .body(customer)
                .when().post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        int nr = 1;

        given().
                contentType(APPLICATION_JSON)
                .when().get("/" + nr)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("firstName", equalTo(customer.getFirstName()))
                .body("lastName", equalTo(customer.getLastName()))
                .body("address.country", equalTo(customer.getAddress().getCountry().toString()))
                .body("address.city", equalTo(customer.getAddress().getCity()))
                .body("address.street", equalTo(customer.getAddress().getStreet()))
                .body("address.postalCode", equalTo(customer.getAddress().getPostalCode()))
                .body("creditCard.expirationMonth", equalTo(customer.getCreditCard().getExpirationMonth()))
                .body("creditCard.expirationYear", equalTo(customer.getCreditCard().getExpirationYear()))
                .body("creditCard.number", equalTo(customer.getCreditCard().getNumber()))
                .body("creditCard.type", equalTo(customer.getCreditCard().getType().toString()))
        ;

    }

    @Test
    public void shouldNotFindCustomer() {

        given().
                contentType(APPLICATION_JSON)
                .when().get("/" + 9000)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }


    @Test
    public void shouldRegisterCustomer() {
        CustomerDTO customer = createCustomerDTO();

        given().
                accept(TEXT_PLAIN)
                .contentType(APPLICATION_JSON)
                .header("password", password)
                .body(customer)
                .when().post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        given().
                accept(TEXT_PLAIN)
                .contentType(APPLICATION_JSON)
                .header("password", password)
                .body(customer)
                .when().post()
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());

    }

    @Test
    public void shouldFailRegisterCustomer() {
        CustomerDTO customer = createCustomerDTO();

        given().
                accept(TEXT_PLAIN)
                .contentType(APPLICATION_JSON)
                .body(customer)
                .when().post()
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());

        customer.setEmail(null);

        given().
                accept(TEXT_PLAIN)
                .contentType(APPLICATION_JSON)
                .header("password", password)
                .body(customer)
                .when().post()
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void searchCustomers() {
    }

    @Test
    public void updateCustomer() {
    }

    private CustomerDTO createCustomerDTO() {
        CustomerDTO cust  = new CustomerDTO();
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