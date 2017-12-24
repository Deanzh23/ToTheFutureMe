package com.dean.tothefutureme.auth.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dean.android.framework.convenient.database.annotation.Column;
import com.dean.android.framework.convenient.database.annotation.PrimaryKey;
import com.dean.tothefutureme.BR;

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
    /**
     * 修改密码时用于表示旧密码
     */
    private String oldPasswrod;
    /**
     * 头像URL
     */
    @Column
    private String avatarUrl = "";
    /**
     * 昵称
     */
    @Column
    private String nickname;
    /**
     * 性别：1->男；0->女
     */
    @Column
    private int genderCode = 9;
    /**
     * 出生日期
     */
    @Column
    private long birthday = 0;
    @Column
    private String token;
    /**
     * 注册验证码
     */
    private String verificationCode;
    /**
     * 编辑模式
     */
    private boolean editModel = false;

    public AuthModel() {
    }

    public AuthModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getOldPasswrod() {
        return oldPasswrod;
    }

    public void setOldPasswrod(String oldPasswrod) {
        this.oldPasswrod = oldPasswrod;
        notifyPropertyChanged(BR.oldPasswrod);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    @Bindable
    public int getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(int genderCode) {
        this.genderCode = genderCode;
        notifyPropertyChanged(BR.genderCode);
    }

    @Bindable
    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
        notifyPropertyChanged(BR.birthday);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Bindable
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        notifyPropertyChanged(BR.verificationCode);
    }

    public boolean isEditModel() {
        return editModel;
    }

    @Bindable
    public boolean getEditModel() {
        return editModel;
    }

    public void setEditModel(boolean editModel) {
        this.editModel = editModel;
        notifyPropertyChanged(BR.editModel);
    }
}
