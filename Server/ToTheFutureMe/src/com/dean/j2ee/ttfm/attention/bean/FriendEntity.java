package com.dean.j2ee.ttfm.attention.bean;

import javax.persistence.*;

/**
 * 好友 Model
 */
@Entity
@Table(name = "friend", schema = "ttfm_db", catalog = "")
@IdClass(FriendEntityPK.class)
public class FriendEntity {

    private String whoFriend;
    private String username;
    private String nickname;
    private String avatarUrl;

    @Id
    @Column(name = "whoFriend")
    public String getWhoFriend() {
        return whoFriend;
    }

    public void setWhoFriend(String whoFriend) {
        this.whoFriend = whoFriend;
    }

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "avatarUrl")
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendEntity that = (FriendEntity) o;

        if (whoFriend != null ? !whoFriend.equals(that.whoFriend) : that.whoFriend != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (avatarUrl != null ? !avatarUrl.equals(that.avatarUrl) : that.avatarUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = whoFriend != null ? whoFriend.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (avatarUrl != null ? avatarUrl.hashCode() : 0);
        return result;
    }
}
