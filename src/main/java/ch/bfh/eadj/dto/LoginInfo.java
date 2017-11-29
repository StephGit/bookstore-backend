package ch.bfh.eadj.dto;

import ch.bfh.eadj.entity.UserGroup;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    private Long nr;
    private String userName;
    private UserGroup userGroup;
    private String password;

    public LoginInfo(Long nr, String userName, UserGroup userGroup, String password) {
        this.nr = nr;
        this.userName = userName;
        this.userGroup = userGroup;
        this.password = password;
    }

    public Long getNr() {
        return nr;
    }

    public String getUserName() {
        return userName;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public String getPassword() { return password; }
}
