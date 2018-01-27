package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;

import javax.ejb.Remote;
import javax.resource.spi.IllegalStateException;
import java.util.List;

/**
 * The customer service provides methods to manage the customers of a bookstore.
 */
@Remote
public interface CustomerServiceRemote {

    Long authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException;

    void changePassword(String email, String password) throws CustomerNotFoundException;

    Customer findCustomer(Long nr) throws CustomerNotFoundException;

    Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException;

    void removeCustomer(Customer customer) throws CustomerNotFoundException;

    List<CustomerInfo> searchCustomers(String name);

    void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException, IllegalStateException;
}
