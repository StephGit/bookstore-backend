package ch.bfh.eadj.persistence.dto;

import java.io.Serializable;

public class CustomerInfo implements Serializable {

    private final Long nr;
    private final String firstName;
    private final String lastName;
    private final String email;

    public CustomerInfo(Long nr, String firstName, String lastName, String email) {
        this.nr = nr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
