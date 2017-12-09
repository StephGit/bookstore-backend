package ch.bfh.eadj.persistence.entity;

import ch.bfh.eadj.persistence.enumeration.UserGroup;

import javax.persistence.*;

@Table(name = "T_LOGIN")
@Entity
@NamedQueries({
        @NamedQuery(name = Login.FIND_BY_USERNAME_QUERY.QUERY_NAME, query = Login.FIND_BY_USERNAME_QUERY.QUERY_STRING)
})
public class Login extends BaseEntity {

    public static class FIND_BY_USERNAME_QUERY {
        public static final String QUERY_NAME = "Login.findByUsername";
        public static final String PARAM_USERNAME = "username";
        public static final String QUERY_STRING = "select l from Login l where l.username = :username";
    }

    @Column(name = "USER_NAME", nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /*
   Wenn Enum (nicht am Ende) hinzugefügt wird gerät die Reihenfolge durcheinander und die
   persistierten ordinalen Werte stimmen nicht mehr
   Eine bessere Lösung ist das Speichern des Namens als String anstelle des ordinalen Wertes.
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_GROUP", nullable = false)
    private UserGroup group;

    public Login() {}

    public Login(String username, String password, UserGroup group) {
        this.username = username;
        this.password = password;
        this.group = group;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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
