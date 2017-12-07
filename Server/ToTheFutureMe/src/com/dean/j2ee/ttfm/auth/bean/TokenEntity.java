package com.dean.j2ee.ttfm.auth.bean;

import javax.persistence.*;

/**
 * token
 */
@Entity
@Table(name = "token", schema = "ttfm_db", catalog = "")
@IdClass(TokenEntityPK.class)
public class TokenEntity {

    private String username;
    private String token;
    private long time;

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "time")
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenEntity that = (TokenEntity) o;

        if (time != that.time) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
