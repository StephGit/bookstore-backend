package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Login;
import ch.bfh.eadj.persistence.enumeration.UserGroup;
import ch.bfh.eadj.persistence.repository.CustomerRepository;
import ch.bfh.eadj.persistence.repository.LoginRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.resource.spi.IllegalStateException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Stateless(name = "CustomerService")
@LocalBean
public class CustomerService implements CustomerServiceRemote {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
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
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, EmailAlreadyUsedException, IllegalStateException {

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
        //get db-login-entry
        Set<Login> loginSet = loginRepository.findByUsername(customerDb.getEmail());
        if (!loginSet.isEmpty() && loginSet.size()==1) {
            Iterator<Login> it = loginSet.iterator();
            Login login = it.next();
            loginRepository.edit(login);
            customerRepository.edit(customer);
        } else {
            throw new IllegalStateException("Failed to update login and customer");
        }


    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {

        Set<Login> loginSet = loginRepository.findByUsername(email);

        if (!loginSet.isEmpty()) {
            for (Login login : loginSet) {
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
        if (login!=null) {
            loginRepository.remove(login);
            customerRepository.remove(customer);
        } else {
            throw new CustomerNotFoundException();
        }
    }
}

