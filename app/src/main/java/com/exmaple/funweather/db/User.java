package com.exmaple.funweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by EthanWalker on 2017/6/17.
 */

public class User extends DataSupport{

    private String username;

    private String password;

    private String personalSign;

    private String phone;

    private String email;

    private String headImgUrl;

    private String backImgUrl;


    public User(String username, String password, String personalSign, String phone, String email, String headImgUrl,String backImgUrl) {
        this.username = username;
        this.password = password;
        this.personalSign = personalSign;
        this.phone = phone;
        this.email = email;
        this.headImgUrl = headImgUrl;
        this.backImgUrl = backImgUrl;

    }

    public String getBackImgUrl() {
        return backImgUrl;
    }

    public void setBackImgUrl(String backImgUrl) {
        this.backImgUrl = backImgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonalSign() {
        return personalSign;
    }

    public void setPersonalSign(String personalSign) {
        this.personalSign = personalSign;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
