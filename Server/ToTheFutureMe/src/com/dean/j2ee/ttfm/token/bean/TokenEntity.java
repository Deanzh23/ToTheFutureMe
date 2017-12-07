package com.dean.j2ee.ttfm.token.bean;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Token实例
 * <p>
 * Created by Dean on 2016/11/30.
 */
@Entity
@Table(name = "token", schema = "most_campus_db", catalog = "")
public class TokenEntity {

    private String userId;
    private String token;
    private Timestamp time;

    @Id
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenEntity that = (TokenEntity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return time != null ? time.equals(that.time) : that.time == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
