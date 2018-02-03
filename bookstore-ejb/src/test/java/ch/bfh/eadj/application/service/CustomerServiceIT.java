package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.resource.spi.IllegalStateException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class CustomerServiceIT extends AbstractServiceIT {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-app-1.0-SNAPSHOT/bookstore/CustomerService!ch.bfh.eadj.application.service.CustomerServiceRemote";

    private static CustomerServiceRemote customerService;

    private Customer customer;
    private String password = "1234asdf";
    private Long userId;
    private Long userId2;

    @BeforeAll
    static void setUp() throws Exception {
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

    @Test
    public void shouldFailRegisterCustomer() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();

        //when
        customerService.registerCustomer(customer, password);

        Executable registerCustomer = () -> customerService.registerCustomer(customer, password);
        assertThrows(EmailAlreadyUsedException.class, registerCustomer);
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

    @Test
    public void shouldFailAuthenticateCustomer() throws CustomerNotFoundException, InvalidPasswordException, EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customerService.registerCustomer(customer, password);

        //when
        Executable executable = () -> customerService.authenticateCustomer(customer.getEmail(), "qwer");
        assertThrows(InvalidPasswordException.class, executable);
    }

    @Test
    public void shouldFailAuthenticateUnknownUser() throws InvalidPasswordException, CustomerNotFoundException {
        //given
        customer = createCustomer();

        //when
        Executable cancelOrder = () -> customerService.authenticateCustomer(customer.getEmail(), "qwer");
        assertThrows(CustomerNotFoundException.class, cancelOrder);

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

    @Test
    public void shouldFailFindCustomer() throws CustomerNotFoundException {
        //given
        Long unknownId = 2321L;

        //when
        Executable findCustomer = () -> customerService.findCustomer(unknownId);
        assertThrows(CustomerNotFoundException.class, findCustomer);
        ;
    }

    @Test
    public void shouldSearchCustomersByFullName() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customer.setEmail("reto" + Integer.toString(new Random().nextInt(10000)) + "@krebs.ch");
        customer.setFirstName("Reto");
        customer.setLastName("Krebs");
        customerService.registerCustomer(customer, password);

        //when
        List<CustomerInfo> result = customerService.searchCustomers(customer.getFirstName() + " " + customer.getLastName());

        //then
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getEmail().contains("@krebs.ch"));
    }

    @Test
    public void shouldSearchCustomersByNamePart() throws EmailAlreadyUsedException {
        //given
        customer = createCustomer();
        customer.setLastName("Bollinger");
        customer.setEmail("bolli" + Integer.toString(new Random().nextInt(10000)) + "@zueri.ch");
        customerService.registerCustomer(customer, password);

        //when
        List<CustomerInfo> result = customerService.searchCustomers(customer.getLastName());

        //then
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getEmail().contains("bolli"));
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
        userId = customerService.registerCustomer(customer, password);
        customer = customerService.findCustomer(userId);
        customer.setEmail("new@mail.com");
        customer.setFirstName("Anton");

        //when
        customerService.updateCustomer(customer);
        Customer updatedCustomer = customerService.findCustomer(userId);

        //then
        assertEquals(updatedCustomer.getNr(), customer.getNr());
        assertEquals(updatedCustomer.getEmail(), customer.getEmail());
        assertEquals(updatedCustomer.getFirstName(), customer.getFirstName());
    }

    @Test
    public void shouldFailUpdateCustomer() throws CustomerNotFoundException, EmailAlreadyUsedException, IllegalStateException {
        //given
        customer = createCustomer();
        userId = customerService.registerCustomer(customer, password);
        Customer newCustomer = createCustomer();
        newCustomer.setLastName("Neuer");
        newCustomer.setFirstName("Max");
        newCustomer.setEmail("hans" + Integer.toString(new Random().nextInt(10000)) + "@dampf.ch");
        userId2 = customerService.registerCustomer(newCustomer, password);
        customer = customerService.findCustomer(userId);
        customer.setEmail(newCustomer.getEmail());

        //when
        Executable executable = () -> customerService.updateCustomer(customer);
        assertThrows(EmailAlreadyUsedException.class, executable);
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

    @Test
    public void shouldFailChangePassword() throws CustomerNotFoundException {
        //when
        Executable executable = () -> customerService.changePassword("new@some.org", password);
        assertThrows(CustomerNotFoundException.class, executable);
    }
}