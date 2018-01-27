package ch.bfh.eadj.persistence.entity;

import ch.bfh.eadj.persistence.enumeration.Country;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {


    private String street;
    private String city;
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private Country country;

    public Address() {}

    public Address(String street, String city, String postalCode, Country country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getStreet() { return street; }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
