package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by 140179 on 2015-06-16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private int id;
    private String mail;
    @JsonProperty("created_on") private String createdOn;
    @JsonProperty("last_login_on") private String lastLoginOn;
    private String lastname;
    private String login;
    private String firstname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(String lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", lastLoginOn='" + lastLoginOn + '\'' +
                ", lastname='" + lastname + '\'' +
                ", login='" + login + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }
}
