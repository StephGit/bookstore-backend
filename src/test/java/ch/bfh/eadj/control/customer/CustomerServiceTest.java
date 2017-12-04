package ch.bfh.eadj.control.customer;


import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.control.exception.CustomerNotFoundException;
import ch.bfh.eadj.control.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.control.exception.InvalidPasswordException;
import ch.bfh.eadj.dto.CustomerInfo;
import ch.bfh.eadj.entity.Customer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;

public class CustomerServiceTest extends AbstractTest {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CustomerService";

    private CustomerServiceRemote customerService;

    private Customer customer;
    private String password = "1234asdf";
    private Long userId;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContect = new InitialContext();
        customerService = (CustomerServiceRemote) jndiContect.lookup(CUSTOMER_SERVICE_NAME);

        customer = new Customer();
        customer.setEmail("hans@dampf.ch");
        customer.setFirstName("Hans");
        customer.setLastName("Dampf");
    }

    @Test
    public void shouldRegisterCustomer() throws EmailAlreadyUsedException {
        //when
        userId = customerService.registerCustomer(customer, password);

        //then
        assertNotNull(userId);
        try {
            customerService.registerCustomer(customer, password);
            fail("EmailAlreadyUsedException exception");
        } catch (EmailAlreadyUsedException e) {
        }
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldAuthenticateCustomer() throws CustomerNotFoundException, InvalidPasswordException {
        //when
        userId = customerService.authenticateCustomer(customer.getEmail(), password);

        //then
        assertNotNull(userId);
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFailAuthenticateCustomer() throws InvalidPasswordException, CustomerNotFoundException {
        //when
        try {
            userId = customerService.authenticateCustomer(customer.getEmail(), "qwer");

            //then
            fail("InvalidPasswordException exception");
        } catch (InvalidPasswordException e) {}
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFailAuthenticateUnknownUser() throws InvalidPasswordException, CustomerNotFoundException {
        //when
        try {
            userId = customerService.authenticateCustomer("asdf@basd.org", "qwer");

            //then
            fail("CustomerNotFoundException exception");
        } catch (CustomerNotFoundException e) {}
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFindCustomer() throws CustomerNotFoundException {

        //when
        Customer foundCustomer = customerService.findCustomer(customer.getNr());

        //then
        assertEquals(foundCustomer.getLastName(), customer.getLastName());
        assertEquals(foundCustomer.getEmail(), customer.getEmail());

    }

    @Test(dependsOnMethods = "registerCustomer")
    public void shouldFailFindCustomer() throws CustomerNotFoundException {
        //when
        try {
            Customer foundCustomer = customerService.findCustomer(2321L);

            //then
            fail("CustomerNotFoundException exception");
        } catch (CustomerNotFoundException e) {}
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldSearchCustomers() {
        //when
        List<CustomerInfo> result = customerService.searchCustomers(customer.getLastName());

        //then
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getEmail(), customer.getEmail());
    }

    @Test
    public void shouldFailSearchCustomers()  {
        //when
        List<CustomerInfo> result = customerService.searchCustomers("Meier");

        //then
        assertTrue(result.isEmpty());
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldUpdateCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException {
        //given
        customer.setEmail("new@mail.com");
        customer.setFirstName("Anton");

        //when
        customerService.updateCustomer(customer);
        Customer updatedCustomer = customerService.findCustomer(customer.getNr());

        //then
        assertEquals(updatedCustomer.getNr(), customer.getNr());
        assertEquals(updatedCustomer.getEmail(), customer.getEmail());
        assertEquals(updatedCustomer.getFirstName(), customer.getFirstName());
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFailUpdateCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException {
        //given
        Customer newCustomer = new Customer();
        newCustomer.setLastName("Neuer");
        newCustomer.setFirstName("Max");
        newCustomer.setEmail("some@mail.com");
        customerService.registerCustomer(newCustomer, password);

        customer.setEmail(newCustomer.getEmail());
        try {
            //when
            customerService.updateCustomer(customer);

            //then
            fail("EmailAlreadyUsedException exception");
        } catch (EmailAlreadyUsedException e) {}

    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldChangePassword() throws Exception {
        //given
        String newPassword = "blab12";

        //when
        customerService.changePassword(customer.getEmail(), newPassword);

        //then
        Long newId = customerService.authenticateCustomer(customer.getEmail(), newPassword);
        assertEquals(customer.getNr(), newId);
    }

    @Test(dependsOnMethods = "shouldChangePassword")
    public void shouldFailChangePassword() throws Exception {
        try {
            //when
            customerService.changePassword("new@some.org", password);

            //then
            fail("CustomerNotFoundException exception");
        } catch (CustomerNotFoundException e) {}
    }

}