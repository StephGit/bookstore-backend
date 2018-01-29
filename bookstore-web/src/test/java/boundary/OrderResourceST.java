package boundary;

import ch.bfh.eadj.boundary.dto.OrderDTO;
import ch.bfh.eadj.boundary.dto.OrderItemDTO;
import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.enumeration.Country;
import ch.bfh.eadj.persistence.enumeration.CreditCardType;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
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

        when().get("orders?customerNr="+1+"&year="+2018)
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
        item.setIsbn("1416902449");
        item.setQuantity(1);
        b.setItems(Arrays.asList(item));

        //create
        given().
                contentType("application/json")
                .body(b)
                .when().post("/orders")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());


        //find
        given().
        when().get("/"+1)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        //update



        //find


        //delete
        given().
                when().delete("/"+1)
                .then()
                .statusCode(Response.Status.OK.getStatusCode());


        //find
        String keywords = "Sapiens: A Brief History of Humankind";
        given().
                when().get("/orders?keywords="+keywords)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("", hasSize(greaterThan(2)));

    }

    private long createCustomer() {


        //TODO mr fischli has a number in his request body...?!

        Customer c = new Customer();
        c.setEmail("zeus"+Integer.toString(new Random().nextInt(10000))+"@gmail.com");
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
        creditCard.setNumber("1234 5622 2222 2222");

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
