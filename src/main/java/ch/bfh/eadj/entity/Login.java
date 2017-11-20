package ch.bfh.eadj.entity;

import javax.persistence.*;

@Table(name = "T_LOGIN")
@Entity
@NamedQuery(name = Login.FIND_BY_NAME_QUERY.QUERY_NAME, query = Login.FIND_BY_NAME_QUERY.QUERY_STRING)
public class Login extends BaseEntity {

    public static class FIND_BY_NAME_QUERY {
        public static final String QUERY_NAME = "Login.findByName";
        public static final String QUERY_STRING = "select l from Login l where l.name = :name";
    }

    @Column(name = "USER_NAME")
    private String name;
    private String password;

    /*
   Wenn Enum (nicht am Ende) hinzugefügt wird gerät die Reihenfolge durcheinander und die
   persistierten ordinalen Werte stimmen nicht mehr
   Eine bessere Lösung ist das Speichern des Namens als String anstelle des ordinalen Wertes.
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_GROUP")
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
