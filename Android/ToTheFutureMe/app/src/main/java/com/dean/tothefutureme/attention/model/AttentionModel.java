package com.dean.tothefutureme.attention.model;

import com.dean.android.framework.convenient.database.annotation.Column;
import com.dean.android.framework.convenient.database.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * 好友Model
 * <p>
 * Created by dean on 2018/1/5.
 */
public class AttentionModel implements Serializable {

    @PrimaryKey
    private String username;
    @PrimaryKey
    private String whoFriend;
    @Column
    private String nickname;
    @Column
    private String avatarUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getWhoFriend() {
        return whoFriend;
    }

    public void setWhoFriend(String whoFriend) {
        this.whoFriend = whoFriend;
    }
}
