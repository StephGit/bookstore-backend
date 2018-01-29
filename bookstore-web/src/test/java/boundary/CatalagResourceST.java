package boundary;

import ch.bfh.eadj.persistence.enumeration.BookBinding;
import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CatalagResourceST {


    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/bookstore/api/books";
    }

    @Test
    public void getBookByIsbn() {

        String isbn = "0099590085";
        when().get("/"+isbn)
                .then()
                .statusCode(200)
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



}
