package entity;

import dto.CustomerInfo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@SqlResultSetMapping(name = "CustomerInfo",
        classes = {
                @ConstructorResult(targetClass = CustomerInfo.class,
                        columns = {
                                @ColumnResult(name = "nr"),
                                @ColumnResult(name = "firstName"),
                                @ColumnResult(name = "lsatName"),
                                @ColumnResult(name = "email")}
                )
        })
public class Customer extends BaseEntity{

  private String firstName;
  private String lastName;
  private String email;

  @OneToMany(mappedBy = "customer")
  //Todo warum Set?
  private Set<Order> order = new HashSet<>(); // Collections immer initialisieren

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
