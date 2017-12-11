package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

public class CustomerServiceIT extends AbstractServiceIT {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-1.0-SNAPSHOT/CustomerService";

    private CustomerServiceRemote customerService;

    private Customer customer;
    private String password = "1234asdf";
    private Long userId;
    private Long userId2;

    @BeforeClass
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        customerService = (CustomerServiceRemote) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
        customer = createCustomer();
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
            System.out.println("Expected exception: EmailAlreadyUsedException");
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
        } catch (InvalidPasswordException e) {
            System.out.println("Expected exception: InvalidPasswordException");
        }
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFailAuthenticateUnknownUser() throws InvalidPasswordException, CustomerNotFoundException {
        //when
        try {
            userId = customerService.authenticateCustomer("asdf@basd.org", "qwer");

            //then
            fail("CustomerNotFoundException exception");
        } catch (CustomerNotFoundException e) {
            System.out.println("Expected exception: CustomerNotFoundException");
        }
    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFindCustomer() throws CustomerNotFoundException {

        //when
        Customer foundCustomer = customerService.findCustomer(userId);

        //then
        assertEquals(foundCustomer.getLastName(), customer.getLastName());
        assertEquals(foundCustomer.getEmail(), customer.getEmail());

    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldFailFindCustomer() throws CustomerNotFoundException {
        //given
        Long unknownId = 2321L;

        //when
        try {
            Customer foundCustomer = customerService.findCustomer(unknownId);

            //then
            fail("CustomerNotFoundException exception");
        } catch (CustomerNotFoundException e) {
            System.out.println("Expected exception: CustomerNotFoundException");
        }
    }

    @Test
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
        customer = customerService.findCustomer(userId);
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

    @Test(dependsOnMethods = {"shouldRegisterCustomer","shouldFindCustomer"})
    public void shouldFailUpdateCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException {
        //given
        Customer newCustomer = createCustomer();
        newCustomer.setLastName("Neuer");
        newCustomer.setFirstName("Max");
        newCustomer.setEmail("some@mail.com");
        userId2 = customerService.registerCustomer(newCustomer, password);
        customer = customerService.findCustomer(userId);
        customer.setEmail(newCustomer.getEmail());
        try {
            //when
            customerService.updateCustomer(customer);

            //then
            fail("EmailAlreadyUsedException exception");
        } catch (EmailAlreadyUsedException e) {
            System.out.println("Expected exception: EmailAlreadyUsedException");
        }

    }

    @Test(dependsOnMethods = "shouldRegisterCustomer")
    public void shouldChangePassword() throws Exception {
        //given
        Long loginId = customerService.authenticateCustomer(customer.getEmail(), password);
        String newPassword = "blab12";

        //when
        customerService.changePassword(customer.getEmail(), newPassword);

        //then
        Long resultLoginId = customerService.authenticateCustomer(customer.getEmail(), newPassword);
        assertEquals(loginId, resultLoginId);
    }

    @Test(dependsOnMethods = "shouldChangePassword")
    public void shouldFailChangePassword() throws Exception {
        try {
            //when
            customerService.changePassword("new@some.org", password);

            //then
            fail("CustomerNotFoundException exception");
        } catch (CustomerNotFoundException e) {
            System.out.println("Expected exception: CustomerNotFoundException");
        }
    }

    @Test(dependsOnMethods = {"shouldRegisterCustomer", "shouldFailUpdateCustomer"})
    public void shouldRemoveCustomer() throws Exception {
        customer = customerService.findCustomer(userId);
        customerService.removeCustomer(customer);
        customer = customerService.findCustomer(userId2);
        customerService.removeCustomer(customer);
    }

}