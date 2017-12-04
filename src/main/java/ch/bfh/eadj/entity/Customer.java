package ch.bfh.eadj.entity;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "T_CUSTOMER")
@Entity
@NamedQueries({
        @NamedQuery(name = Customer.FIND_BY_NAME_QUERY.QUERY_NAME, query = Customer.FIND_BY_NAME_QUERY.QUERY_STRING)
})
public class Customer extends BaseEntity implements Serializable {

    public static class FIND_BY_NAME_QUERY {
        public static final String QUERY_NAME = "customer.findByName";
        public static final String PARAM_NAME = "name";
        public static final String QUERY_STRING = "select new ch.bfh.eadj.dto.CustomerInfo(c.nr, c.firstName, c.lastName, c.email) " +
                "from Customer c where c.firstName like :name or c.lastName like :name";
    }

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

}
