package com.dean.tothefutureme.auth.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dean.android.framework.convenient.database.annotation.Column;
import com.dean.android.framework.convenient.database.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * 用户基本信息Model
 * <p>
 * Created by dean on 2017/12/3.
 */
public class AuthModel extends BaseObservable implements Serializable {

    @PrimaryKey
    private String username;
    @Column
    private String password;
    @Column
    private String token;

    public AuthModel() {
    }

    public AuthModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
