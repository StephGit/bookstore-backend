package ch.bfh.eadj.boundary.dto;

import ch.bfh.eadj.persistence.entity.Address;
import ch.bfh.eadj.persistence.entity.CreditCard;

public class CustomerDTO {

    private Long nr;

    private String firstName;

    private String lastName;

    private String email;

    private Address address;

    private CreditCard creditCard;

    public CustomerDTO() {}

    public CustomerDTO(Long nr, String firstName, String lastName, String email, CreditCard creditCard, Address address) {
        this.nr = nr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.creditCard = creditCard;
        this.address = address;
    }

    public Long getNr() {
        return nr;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setNr(Long nr) {
        this.nr = nr;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
