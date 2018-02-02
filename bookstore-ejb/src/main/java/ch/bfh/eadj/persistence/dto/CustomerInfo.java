package ch.bfh.eadj.persistence.dto;

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

    //Setter for JSON-Parsing in REST-Interface
    public CustomerInfo() {
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

    //Setter for JSON-Parsing in REST-Interface
    public void setNr(Long nr) {
        this.nr = nr;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //Setter for JSON-Parsing in REST-Interface
    public void setEmail(String email) {
        this.email = email;
    }
}
