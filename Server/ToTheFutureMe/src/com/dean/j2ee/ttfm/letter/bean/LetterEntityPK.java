package com.dean.j2ee.ttfm.letter.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class LetterEntityPK implements Serializable {
    private String letterId;
    private String userId;

    @Column(name = "letterId")
    @Id
    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String letterId) {
        this.letterId = letterId;
    }

    @Column(name = "userId")
    @Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LetterEntityPK that = (LetterEntityPK) o;

        if (letterId != null ? !letterId.equals(that.letterId) : that.letterId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = letterId != null ? letterId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
