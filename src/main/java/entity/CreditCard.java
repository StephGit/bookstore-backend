package entity;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class CreditCard {

    /*
    Wenn Enum (nicht am Ende) hinzugefügt wird gerät die Reihenfolge durcheinander und die
    persistierten ordinalen Werte stimmen nicht mehr
    Eine bessere Lösung ist das Speichern des Namens als String anstelle des ordinalen Wertes.
     */
    @Enumerated(EnumType.STRING)
    private CreditCardType creditCardType;


    private String number;
    private Integer expirationMonth;
    private Integer expirationYear;


    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    //TODO equals and hashcode?
}
