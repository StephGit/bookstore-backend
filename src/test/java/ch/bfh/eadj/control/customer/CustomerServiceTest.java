package ch.bfh.eadj.control.customer;


import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.control.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.entity.Customer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class CustomerServiceTest extends AbstractTest {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CustomerService";

    private CustomerServiceRemote customerService;

    private Customer customer;
    private String password = "1234asdf";
    private Long userId;

    @BeforeClass
    public void setUp() throws Exception {

        customerService = (CustomerServiceRemote) new InitialContext().lookup(CUSTOMER_SERVICE_NAME);

        customer = new Customer();
        customer.setEmail("hans@dampf.ch");
        customer.setFirstName("Hans");
        customer.setLastName("Dampf");
    }

    @Test
    public void registerCustomer() throws Exception {
        userId = customerService.registerCustomer(customer, password);
        assertNotNull(userId);
        try {
            customerService.registerCustomer(customer, password);
            fail("EmailAlreadyUsedException exception");
        } catch (EmailAlreadyUsedException e) {
        }

    }

    @Test(dependsOnMethods = "registerCustomer")
    public void authenticateCustomer() throws Exception {
    }

    @Test(dependsOnMethods = "registerCustomer")
    public void findCustomer() throws Exception {
    }

    @Test
    public void searchCustomers() throws Exception {
    }

    @Test
    public void updateCustomer() throws Exception {
    }

    @Test
    public void changePassword() throws Exception {
    }

}