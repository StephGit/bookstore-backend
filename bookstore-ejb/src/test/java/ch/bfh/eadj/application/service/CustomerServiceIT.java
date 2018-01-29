package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.resource.spi.IllegalStateException;

import java.util.List;

import static org.junit.Assert.*;


public class CustomerServiceIT extends AbstractServiceIT {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app-1.0-SNAPSHOT/bookstore/CustomerService!ch.bfh.eadj.application.service.CustomerServiceRemote";

    private CustomerServiceRemote customerService;

    private Customer customer;
    private String password = "1234asdf";
    private Long userId;
    private Long userId2;

    @Before
    public void setUp() throws Exception {
        Context jndiContext = new InitialContext();
        customerService = (CustomerServiceRemote) jndiContext.lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test
    public void shouldRegisterCustomer() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();

        //when
        userId = customerService.registerCustomer(customer, password);

        //then
        assertNotNull(userId);
    }

    @Test(expected = EmailAlreadyUsedException.class)
    public void shouldFailRegisterCustomer() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();

        //when
        customerService.registerCustomer(customer, password);
        customerService.registerCustomer(customer, password);
    }

    @Test
    public void shouldAuthenticateCustomer() throws CustomerNotFoundException, InvalidPasswordException, EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);

        //when
        userId = customerService.authenticateCustomer(customer.getEmail(), password);

        //then
        assertNotNull(userId);
    }

    @Test(expected = InvalidPasswordException.class)
    public void shouldFailAuthenticateCustomer() throws CustomerNotFoundException, InvalidPasswordException, EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);

        //when
        userId = customerService.authenticateCustomer(customer.getEmail(), "qwer");
    }

    @Test(expected = CustomerNotFoundException.class)
    public void shouldFailAuthenticateUnknownUser() throws InvalidPasswordException, CustomerNotFoundException {
        //given
        customer = createCustomer();

        //when
        userId = customerService.authenticateCustomer(customer.getEmail(), "qwer");
    }

    @Test
    public void shouldFindCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        userId = customerService.registerCustomer(customer, password);

        //when
        Customer foundCustomer = customerService.findCustomer(userId);

        //then
        assertEquals(foundCustomer.getLastName(), customer.getLastName());
        assertEquals(foundCustomer.getEmail(), customer.getEmail());

    }

    @Test(expected = CustomerNotFoundException.class)
    public void shouldFailFindCustomer() throws CustomerNotFoundException {
        //given
        Long unknownId = 2321L;

        //when
        customerService.findCustomer(unknownId);
    }

    @Test
    public void shouldSearchCustomersByFullName() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);

        //when
        List<CustomerInfo> result = customerService.searchCustomers(customer.getFirstName() + " " + customer.getLastName());

        //then
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getEmail(), customer.getEmail());
    }

    @Test
    public void shouldSearchCustomersByNamePart() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);

        //when
        List<CustomerInfo> result = customerService.searchCustomers(customer.getLastName());

        //then
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getEmail(), customer.getEmail());
    }

    @Test
    public void shouldFailSearchCustomers() {
        //when
        List<CustomerInfo> result = customerService.searchCustomers("Meierreier");

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdateCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException, IllegalStateException {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);
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

    @Test(expected = EmailAlreadyUsedException.class)
    public void shouldFailUpdateCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException, IllegalStateException {
        //given
        customer = createCustomer();
        userId = customerService.registerCustomer(customer, password);
        Customer newCustomer = createCustomer();
        newCustomer.setLastName("Neuer");
        newCustomer.setFirstName("Max");
        newCustomer.setEmail("some@mail.com");
        userId2 = customerService.registerCustomer(newCustomer, password);
        customer = customerService.findCustomer(userId);
        customer.setEmail(newCustomer.getEmail());

        //when
        customerService.updateCustomer(customer);
    }

    @Test
    public void shouldChangePassword() throws Exception {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);
        Long loginId = customerService.authenticateCustomer(customer.getEmail(), password);
        String newPassword = "blab12";

        //when
        customerService.changePassword(customer.getEmail(), newPassword);

        //then
        Long resultLoginId = customerService.authenticateCustomer(customer.getEmail(), newPassword);
        assertEquals(loginId, resultLoginId);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void shouldFailChangePassword() throws CustomerNotFoundException {
        //when
        customerService.changePassword("new@some.org", password);

        //then
        fail("CustomerNotFoundException exception");
    }
}