package ch.bfh.eadj.application.service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Login;
import ch.bfh.eadj.persistence.enumeration.UserGroup;
import ch.bfh.eadj.persistence.repository.CustomerRepository;
import ch.bfh.eadj.persistence.repository.LoginRepository;

@Stateless
public class CustomerService implements CustomerServiceRemote {

    @EJB
    private CustomerRepository customerRepository;

    @EJB
    private LoginRepository loginRepository;

    @Override
    public Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException {

        Set<Login> loginSet = loginRepository.findByUsername(customer.getEmail());

        if (loginSet.isEmpty()) {
            customerRepository.create(customer);
            Login login = new Login();
            login.setGroup(UserGroup.CUSTOMER);
            login.setPassword(password);
            login.setUsername(customer.getEmail());
            loginRepository.create(login);
            return customer.getNr();
        } else {
            throw new EmailAlreadyUsedException();
        }
    }

    @Override
    public Long authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {

        Set<Login> loginSet = loginRepository.findByUsername(email);

        if (!loginSet.isEmpty()) {
            for (Login login : loginSet) {
                if (password.equals(login.getPassword())) {
                    return login.getNr();
                } else {
                    throw new InvalidPasswordException();
                }
            }
        } else {
            throw new CustomerNotFoundException();
        }
        return null;
    }

    @Override
    public Customer findCustomer(Long nr) throws CustomerNotFoundException {

        Customer customer = customerRepository.find(nr);

        if (customer!=null) {
            return customer;
        } else {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<CustomerInfo> searchCustomers(String name) {

        List<CustomerInfo> customerInfoList = customerRepository.findByName(name);

        if (!customerInfoList.isEmpty()) {
            return customerInfoList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException {

        Customer customerDb = customerRepository.find(customer.getNr());

        if (customerDb==null) {
            throw new CustomerNotFoundException();
        }
        if (!customerDb.getEmail().equals(customer.getEmail())) {
            // check if new Email allready is in use
            Set<Login> loginSet = loginRepository.findByUsername(customer.getEmail());
            if (!loginSet.isEmpty()) {
                throw new EmailAlreadyUsedException();
            }
        }
        customerRepository.edit(customer);
    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {

        Set<Login> loginSet = loginRepository.findByUsername(email);

        if (!loginSet.isEmpty()) {
            for (Iterator<Login> it = loginSet.iterator(); it.hasNext(); ) {
                Login login = it.next();
                login.setPassword(password);
                loginRepository.edit(login);
            }
        } else {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void removeCustomer(Customer customer) throws CustomerNotFoundException {
        Login login = loginRepository.find(customer.getNr());

        loginRepository.remove(login);
        customerRepository.remove(customer);
    }
}

