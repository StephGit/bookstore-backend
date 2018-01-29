package ch.bfh.eadj.boundary.resource;

import ch.bfh.eadj.boundary.dto.CustomerDTO;
import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.enumeration.Country;
import ch.bfh.eadj.persistence.enumeration.CreditCardType;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;


public class CustomerResourceTest {

    private String password = "asdfkjl";

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/bookstore/api/customers";
    }

    @Test
    public void shouldAuthenticateCustomer() {
        CustomerDTO customer = createCustomerDTO();
        addCustomer(customer);

        given().
                header("email", customer.getEmail())
                .header("password", password)
                .when().get("/login")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }


    @Test
    public void shouldFailAuthenticateCustomer() {
        CustomerDTO customer = createCustomerDTO();
        addCustomer(customer);

        given().
                header("email", customer.getEmail())
                .header("password", "jdkdkdj")
                .when().get("/login")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());

        given().
                header("email", "qwerwqer@dr.ch")
                .header("password", "jdkdkdj")
                .when().get("/login")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }



    @Test
    public void shouldChangePassword() {
        CustomerDTO customer = createCustomerDTO();
        addCustomer(customer);

        given().
                accept(APPLICATION_JSON)
                .contentType(TEXT_PLAIN)
                .header("email", customer.getEmail())
                .header("password", password)
                .when().put("/login")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void shouldFailChangePassword() {
        CustomerDTO customer = createCustomerDTO();

        given().
                accept(APPLICATION_JSON)
                .contentType(TEXT_PLAIN)
                .header("email", customer.getEmail())
                .header("password", password)
                .when().put("/login")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldFindCustomer() {
        CustomerDTO customer = createCustomerDTO();

        long id = addCustomer(customer);

        given().
                contentType(APPLICATION_JSON)
                .when().get("/" + id)
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
                .body("creditCard.type", equalTo(customer.getCreditCard().getType().toString()));

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
        CustomerDTO customer = createCustomerDTO();
        addCustomer(customer);

        com.jayway.restassured.response.Response response = given().
                accept(APPLICATION_JSON)
                .when().get("?name=" + customer.getLastName())
                .then()
                .statusCode(Response.Status.OK.getStatusCode()).extract().response();

        ArrayList<Map<String,?>> jsonAsArrayList = from(response.asString()).get("");
        assertFalse(jsonAsArrayList.isEmpty());
    }

    @Test
    public void shouldUpdateCustomer() {

        CustomerDTO customer = createCustomerDTO();
        long id = addCustomer(customer);
        CustomerDTO customerUpdate = new CustomerDTO(id, customer.getFirstName(), "Röhrich", customer.getEmail(), null, null);

        given().
                accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(customerUpdate)
                .when().put("/" + id)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    public void shouldFailUpdateCustomer() {
        long id = 929292;
        CustomerDTO customer = new CustomerDTO(id, "929292", "Peter", "Hans", null, null);

        given().
                accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(customer)
                .when().put("/" + id)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        given().
                accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(customer)
                .when().put("/" + id + 1)
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldFailUpdateCustomerOnConflict() {

        CustomerDTO customer = createCustomerDTO();
        CustomerDTO customer2 = createCustomerDTO();
        long id = addCustomer(customer);
        addCustomer(customer2);
        CustomerDTO customerUpdate = new CustomerDTO(id, customer.getFirstName(), customer.getLastName(), customer2.getEmail(), null, null);

        given().
                accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(customerUpdate)
                .when().put("/" + id)
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }

    private long addCustomer(CustomerDTO customerDTO) {
        com.jayway.restassured.response.Response validatableResponse = given().
                accept(TEXT_PLAIN)
                .contentType(APPLICATION_JSON)
                .header("password", password)
                .body(customerDTO)
                .when().post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).extract().response();

        return Long.parseLong(validatableResponse.getBody().asString());
    }

    private CustomerDTO createCustomerDTO() {
        CustomerDTO cust  = new CustomerDTO();
        cust.setEmail("hans" +Integer.toString(new Random().nextInt(10000))+ "@dampf.ch");
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