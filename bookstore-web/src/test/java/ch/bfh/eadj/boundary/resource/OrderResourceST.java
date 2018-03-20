package ch.bfh.eadj.boundary.resource;

import ch.bfh.eadj.boundary.dto.OrderDTO;
import ch.bfh.eadj.boundary.dto.OrderItemDTO;
import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.enumeration.Country;
import ch.bfh.eadj.persistence.enumeration.CreditCardType;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderResourceST {

    @BeforeAll
    public static void setUpClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/bookstore/api";

    }

    @Test
    public void shouldPlaceOrderAndDeleteIt() {


        long customerId = createCustomer();

        OrderDTO b = new OrderDTO();
        b.setCustomerNr(customerId);
        OrderItemDTO item = new OrderItemDTO();
        String isbn = "1416902449";
        item.setIsbn(isbn);
        item.setQuantity(1);
        b.setItems(Arrays.asList(item));

        //create
        com.jayway.restassured.response.Response response = given().
                contentType("application/json")
                .body(b)
                .when().post("/orders")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).extract().response();

        Integer orderId = response.jsonPath().get("nr");

        //find
        given().
                when().get("orders/" + orderId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("nr", equalTo(orderId))
                .body("items", hasSize(1))
                .body("items[0].book.isbn", equalTo(isbn))
                .body("status",not(equalTo((OrderStatus.ACCEPTED.toString()))))
                .body("status",not(equalTo((OrderStatus.CANCELED.toString()))));


        //delete
        given().
                when().delete("orders/" + orderId)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());


        //find
        given().
                when().get("orders?customerNr="+customerId+"&year="+2018)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("", hasSize(1))
                .body("[0].nr", equalTo(orderId))
                .body("[0].amount", notNullValue())
                .body("[0].date", notNullValue())
                .body("[0].status",equalTo((OrderStatus.CANCELED.toString())));

    }

    private long createCustomer() {
        Customer c = new Customer();
        c.setEmail("zeus" + Integer.toString(new Random().nextInt(10000)) + "@gmail.com");
        c.setFirstName("Sven");
        c.setLastName("Hotz");

        Address address = new Address();
        address.setCity("bern");
        address.setCountry(Country.CH);
        address.setPostalCode("3000");
        address.setStreet("test");
        c.setAddress(address);

        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationYear(2019);
        creditCard.setExpirationMonth(12);
        creditCard.setType(CreditCardType.MASTERCARD);
        creditCard.setNumber("1234562222222222");

        c.setCreditCard(creditCard);

        com.jayway.restassured.response.Response validatableResponse = given().
                contentType("application/json")
                .accept("text/plain")
                .header(new Header("password", "root"))
                .body(c)
                .when().post("/customers")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode()).extract().response();

        return Long.parseLong(validatableResponse.getBody().asString());

    }


}
