package com.tusofia.ndurmush.business.user;

import com.tusofia.ndurmush.business.user.entity.User;

public class UserRegisterRequest extends User {

    private String password;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    
}
