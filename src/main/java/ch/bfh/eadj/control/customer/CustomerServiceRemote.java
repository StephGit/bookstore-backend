package ch.bfh.eadj.control.customer;

import ch.bfh.eadj.control.exception.CustomerNotFoundException;
import ch.bfh.eadj.control.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.control.exception.InvalidPasswordException;
import ch.bfh.eadj.dto.CustomerInfo;
import ch.bfh.eadj.entity.Customer;

import javax.ejb.Remote;
import java.util.List;

/**
 * The customer service provides methods to manage the customers of a bookstore.
 */
@Remote
public interface CustomerServiceRemote {

    public Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException;

    public Long authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException;

    public Customer findCustomer(Long nr) throws CustomerNotFoundException;

    List<CustomerInfo> searchCustomers(String name);

    void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException;

    void changePassword(String email, String password) throws CustomerNotFoundException;
}
