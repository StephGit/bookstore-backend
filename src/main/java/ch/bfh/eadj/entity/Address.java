package ch.bfh.eadj.entity;

import javax.persistence.*;

@Embeddable
public class Address {

    private String street;
    private String city;
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private Country country;

    public Address() {}

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
