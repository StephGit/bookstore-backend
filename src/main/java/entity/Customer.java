package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

@Table(name = "T_CUSTOMER")
@Entity
@NamedQueries({
        @NamedQuery(name = Customer.FIND_BY_NAME_QUERY.QUERY_NAME, query = Customer.FIND_BY_NAME_QUERY.QUERY_STRING)
})
public class Customer extends BaseEntity {

    public static class FIND_BY_NAME_QUERY {
        public static final String QUERY_NAME = "Customer.findByName";
        public static final String QUERY_STRING = "select new dto.CustomerInfo(c.id, c.firstName, c.lastName, c.email) " +
                "from Customer c where c.fistName in :name or c.lastName in :name";
    }

    private String firstName;
    private String lastName;
    private String email;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null) return false;
        if (lastName != null ? !lastName.equals(customer.lastName) : customer.lastName != null) return false;
        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
        if (address != null ? !address.equals(customer.address) : customer.address != null) return false;
        return creditCard != null ? creditCard.equals(customer.creditCard) : customer.creditCard == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (creditCard != null ? creditCard.hashCode() : 0);
        return result;
    }
}
