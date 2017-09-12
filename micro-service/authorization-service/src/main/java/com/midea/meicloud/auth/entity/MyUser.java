package com.midea.meicloud.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" ,"fieldHandler"})
public class MyUser{

    @Id
    @GeneratedValue
    private Long id;
    //用户名
    @NotEmpty(message = "用户名不能为空")
    private String username;
    //密码
    @NotEmpty(message = "密码不能为空")
    private String password;
    // 一个用户具有多个角色
    @OneToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.LAZY )
    private List<MyRole> myRoles;
    //最后登录时间
    private Date lastLoginDate;
    //电话号码
    private String phoneNo;
    //当前是否在线
    private boolean isOnLine;
    //email
    private String emailAddr;

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MyRole> getMyRoles() {
        return myRoles;
    }

    public void setMyRoles(List<MyRole> myRoles) {
        this.myRoles = myRoles;
    }
}
