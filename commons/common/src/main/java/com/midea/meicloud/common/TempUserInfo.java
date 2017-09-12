package com.midea.meicloud.common;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TempUserInfo implements Serializable {
    private String username;
    private Date lastRequestDate;
    private Long userid;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public TempUserInfo(String username){
        this.username = username;
        lastRequestDate = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastRequestDate() {
        return lastRequestDate;
    }

    public void setLastRequestDate(Date lastRequestDate) {
        this.lastRequestDate = lastRequestDate;
    }
}
