package ch.bfh.eadj.application.service;


import ch.bfh.eadj.application.service.CatalogServiceRemote;
import ch.bfh.eadj.persistence.entity.Book;
import org.testng.annotations.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;

public class OrderServiceIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/OrderService";

    private CatalogServiceRemote orderService;
    private Book book;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        orderService = (CatalogServiceRemote) jndiContext.lookup(ORDER_SERVICE_NAME);
    }


}