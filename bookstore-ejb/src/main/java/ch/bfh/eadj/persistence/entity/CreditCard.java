package ch.bfh.eadj.persistence.entity;

import ch.bfh.eadj.persistence.enumeration.CreditCardType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class CreditCard implements Serializable {

    /*
    Wenn Enum (nicht am Ende) hinzugefügt wird gerät die Reihenfolge durcheinander und die
    persistierten ordinalen Werte stimmen nicht mehr
    Eine bessere Lösung ist das Speichern des Namens als String anstelle des ordinalen Wertes.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "CC_TYPE", nullable = false)
    private CreditCardType type;

    @Column(name = "CC_NUMBER", nullable = false)
    private String number;

    @Column(nullable = false)
    private Integer expirationMonth;
    
    @Column(nullable = false)
    private Integer expirationYear;


    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
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
    
}
