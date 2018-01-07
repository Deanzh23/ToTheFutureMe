package com.dean.j2ee.ttfm.attention.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class FriendEntityPK implements Serializable {
    private String whoFriend;
    private String username;

    @Column(name = "whoFriend")
    @Id
    public String getWhoFriend() {
        return whoFriend;
    }

    public void setWhoFriend(String whoFriend) {
        this.whoFriend = whoFriend;
    }

    @Column(name = "username")
    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendEntityPK that = (FriendEntityPK) o;

        if (whoFriend != null ? !whoFriend.equals(that.whoFriend) : that.whoFriend != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = whoFriend != null ? whoFriend.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
