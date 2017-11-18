package entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Login extends BaseEntity {

    private String name;
    private String password;

    /*
   Wenn Enum (nicht am Ende) hinzugefügt wird gerät die Reihenfolge durcheinander und die
   persistierten ordinalen Werte stimmen nicht mehr
   Eine bessere Lösung ist das Speichern des Namens als String anstelle des ordinalen Wertes.
    */
    @Enumerated(EnumType.STRING)
    private UserGroup group;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }
}
