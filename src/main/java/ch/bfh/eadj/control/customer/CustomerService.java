package ch.bfh.eadj.control.customer;

import ch.bfh.eadj.control.LoginRepository;
import ch.bfh.eadj.control.exception.CustomerNotFoundException;
import ch.bfh.eadj.control.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.control.exception.InvalidPasswordException;
import ch.bfh.eadj.dto.CustomerInfo;
import ch.bfh.eadj.dto.LoginInfo;
import ch.bfh.eadj.entity.Customer;
import ch.bfh.eadj.entity.Login;
import ch.bfh.eadj.entity.UserGroup;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Stateless
public class CustomerService implements CustomerServiceRemote {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private LoginRepository loginRepository;

    @Override
    public Long registerCustomer(Customer customer, String password) throws EmailAlreadyUsedException {

        Set<LoginInfo> loginInfoSet = loginRepository.findLoginByUserName(customer.getEmail());

        if (loginInfoSet.isEmpty()) {
            customerRepository.create(customer);
            Login login = new Login();
            login.setGroup(UserGroup.CUSTOMER);
            login.setPassword(password);
            login.setUserName(customer.getEmail());
            loginRepository.create(login);
            return login.getNr();
        } else {
            throw new EmailAlreadyUsedException();
        }
    }

    @Override
    public Long authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {

        Set<LoginInfo> loginInfoSet = loginRepository.findLoginByUserName(email);

        if (!loginInfoSet.isEmpty()) {

            for (Iterator<LoginInfo> it = loginInfoSet.iterator(); it.hasNext(); ) {
                LoginInfo loginInfo = it.next();

                if (password.equals(loginInfo.getPassword())) {
                    return loginInfo.getNr();
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

        List<CustomerInfo> customerInfoList = customerRepository.findCustomerByName(name);

        if (!customerInfoList.isEmpty()) {
            return customerInfoList;
        } else {
            return null;
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
            Set<LoginInfo> loginInfoSet = loginRepository.findLoginByUserName(customer.getEmail());

            if (!loginInfoSet.isEmpty()) {
                throw new EmailAlreadyUsedException();
            }
        }

        customerRepository.edit(customer);

    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {

        Set<LoginInfo> loginInfoSet = loginRepository.findLoginByUserName(email);

        if (!loginInfoSet.isEmpty()) {

            for (Iterator<LoginInfo> it = loginInfoSet.iterator(); it.hasNext(); ) {
                LoginInfo loginInfo = it.next();

                Login login = new Login();
                login.setUserName(loginInfo.getUserName());
                login.setPassword(password);
                login.setGroup(loginInfo.getUserGroup());

                loginRepository.edit(login);

            }
        } else {
            throw new CustomerNotFoundException();
        }
    }
}

