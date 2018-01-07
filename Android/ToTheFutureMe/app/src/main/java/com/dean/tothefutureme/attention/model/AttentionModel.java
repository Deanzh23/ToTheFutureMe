package com.dean.tothefutureme.attention.model;

import java.io.Serializable;

/**
 * 好友Model
 * <p>
 * Created by dean on 2018/1/5.
 */
public class AttentionModel implements Serializable {

    private String username;

    private String nickname;

    private String avatarUrl;

    private String whoFriend;

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
