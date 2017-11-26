package ch.bfh.eadj.dto;

import java.io.Serializable;

public class CustomerInfo implements Serializable {

    private Long nr;
    private String firstName;
    private String lastName;
    private String email;

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
