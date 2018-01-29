package boundary;

import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.enumeration.BookBinding;
import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class CatalagResourceST {


    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/bookstore/api/books";
    }

    @Test
    public void shoudFindBookByIsbn() {

        String isbn = "0099590085";
        when().get("/"+isbn)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("isbn", equalTo(isbn))
                .body("binding", equalTo(BookBinding.PAPERBACK.toString()))
                .body("numberOfPages", equalTo(434))
                .body("title", equalTo("Sapiens: A Brief History of Humankind"))
                .body("description", notNullValue())
                .body("authors", notNullValue())
                .body("price", notNullValue())
                .body("imageUrl", notNullValue())
                .body("publisher", notNullValue());

    }

    @Test
    public void shouldNotFindBookByIsbn() {

        String isbn = "0000000001";
        when().get("/"+isbn)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


    @Test
    public void shoudFindBookByKeywords() {

        String keywords = "Sapiens: A Brief History of Humankind";
        when().get("?keywords="+keywords)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("", hasSize(greaterThan(2)));

    }


    @Test
    public void shoudFindNoBookByKeywords() {

        String keywords = "qewqwweqwe qeqweqw qweqweqweqweq";
        when().get("?keywords="+keywords)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("", hasSize(0));

    }

    @Test
    public void shouldAddBook() {

        String isbn = Integer.toString(new Random().nextInt(10000));

        Book b = new Book();
        b.setIsbn(isbn);
        b.setTitle("sapiens");
        b.setPrice(BigDecimal.ONE);

        given().
                contentType("application/json")
                .body(b)
                .when().post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

        // try to add same book twice
        given().
                contentType("application/json").
                body(b).
                when().post()
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode());
    }


    @Test
    public void shouldUpdateBook() {

        String isbn = Integer.toString(new Random().nextInt(10000));

        Book b = new Book();
        b.setIsbn(isbn);
        b.setTitle("sapiens");
        b.setPrice(BigDecimal.ONE);

        //create
        given().
                contentType("application/json")
                .body(b)
                .when().post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());



        //update
        b.setPrice(new BigDecimal("999.99"));
        given().
                contentType("application/json").
                body(b).
                when().put("/"+isbn)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());


    }





}
