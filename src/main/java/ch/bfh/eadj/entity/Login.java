package ch.bfh.eadj.entity;

import javax.persistence.*;

@Table(name = "T_LOGIN")
@Entity
@NamedQueries({
        @NamedQuery(name = Login.FIND_BY_USERNAME_QUERY.QUERY_NAME, query = Login.FIND_BY_USERNAME_QUERY.QUERY_STRING)
})
public class Login extends BaseEntity {

    public static class FIND_BY_USERNAME_QUERY {
        public static final String QUERY_NAME = "Login.findByUserName";
        public static final String QUERY_STRING = "select new ch.bfh.eadj.dto.LoginInfo(l.nr, l.userName, l.group) from Login l where l.userName = :userName";
    }

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

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


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
