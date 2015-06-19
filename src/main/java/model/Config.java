package model;

import java.io.Serializable;

/**
 * Created by 140179 on 2015-06-17.
 */
public class Config implements Serializable {
    private String id;
    private String pwd;
    private String lastLoginTime;

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
