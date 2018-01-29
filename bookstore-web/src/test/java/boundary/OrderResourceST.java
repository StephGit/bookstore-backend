package boundary;

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
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class OrderResourceST {

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/bookstore/api";

    }

    @Test
    public void shouldSearchOrder() {

        when().get("orders?customerNr=" + 1 + "&year=" + 2018)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("", hasSize(0));

    }


    @Test
    public void shouldPlaceOrder() {


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
                .statusCode(Response.Status.OK.getStatusCode()).extract().response();


        Integer orderId = response.jsonPath().get("nr");


        //find
        given().
                when().get("orders/" + orderId)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("nr", equalTo(orderId))
                .body("items", hasSize(1))
                .body("items[0].book.isbn", equalTo(isbn))
                .body("customer.nr", equalTo(customerId))
                .body("status",equalTo(OrderStatus.SHIPPED.toString()));

        //update


        //find


        //delete
        given().
                when().delete("/" + 1)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());


        //find
        String keywords = "Sapiens: A Brief History of Humankind";
        given().
                when().get("/orders?keywords=" + keywords)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("", hasSize(greaterThan(2)));

    }

    private long createCustomer() {


        //TODO mr fischli has a number in his request body...?!

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
