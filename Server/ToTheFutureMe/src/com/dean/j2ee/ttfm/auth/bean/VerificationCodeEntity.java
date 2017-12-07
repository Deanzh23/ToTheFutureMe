package com.dean.j2ee.ttfm.auth.bean;

import javax.persistence.*;

/**
 * 验证码
 */
@Entity
@Table(name = "verificationCode", schema = "ttfm_db", catalog = "")
public class VerificationCodeEntity {

    private String username;
    private String verificationCode;
    private long time;

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "verificationCode")
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
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

        VerificationCodeEntity that = (VerificationCodeEntity) o;

        if (time != that.time) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (verificationCode != null ? !verificationCode.equals(that.verificationCode) : that.verificationCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (verificationCode != null ? verificationCode.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
