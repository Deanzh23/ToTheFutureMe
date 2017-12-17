package com.dean.j2ee.ttfm.letter.bean;

import javax.persistence.*;

@Entity
@Table(name = "letter", schema = "ttfm_db", catalog = "")
@IdClass(LetterEntityPK.class)
public class LetterEntity {

    private String letterId;
    private String userId;
    private Integer type;
    private String senderUserId;
    private String senderUserNickName;
    private String senderAvatarUrl;
    private long sendDateTime;
    private long receiveDateTime;
    private String content;
    private int isRead;

    @Id
    @Column(name = "letterId")
    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String letterId) {
        this.letterId = letterId;
    }

    @Id
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "senderUserId")
    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    @Basic
    @Column(name = "senderUserNickName")
    public String getSenderUserNickName() {
        return senderUserNickName;
    }

    public void setSenderUserNickName(String senderUserNickName) {
        this.senderUserNickName = senderUserNickName;
    }

    @Basic
    @Column(name = "senderAvatarUrl")
    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    @Basic
    @Column(name = "sendDateTime")
    public long getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(long sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    @Basic
    @Column(name = "receiveDateTime")
    public long getReceiveDateTime() {
        return receiveDateTime;
    }

    public void setReceiveDateTime(long receiveDateTime) {
        this.receiveDateTime = receiveDateTime;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "isRead")
    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LetterEntity that = (LetterEntity) o;

        if (sendDateTime != that.sendDateTime) return false;
        if (receiveDateTime != that.receiveDateTime) return false;
        if (isRead != that.isRead) return false;
        if (letterId != null ? !letterId.equals(that.letterId) : that.letterId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (senderUserId != null ? !senderUserId.equals(that.senderUserId) : that.senderUserId != null) return false;
        if (senderUserNickName != null ? !senderUserNickName.equals(that.senderUserNickName) : that.senderUserNickName != null) return false;
        if (senderAvatarUrl != null ? !senderAvatarUrl.equals(that.senderAvatarUrl) : that.senderAvatarUrl != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = letterId != null ? letterId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (senderUserId != null ? senderUserId.hashCode() : 0);
        result = 31 * result + (senderUserNickName != null ? senderUserNickName.hashCode() : 0);
        result = 31 * result + (senderAvatarUrl != null ? senderAvatarUrl.hashCode() : 0);
        result = 31 * result + (int) (sendDateTime ^ (sendDateTime >>> 32));
        result = 31 * result + (int) (receiveDateTime ^ (receiveDateTime >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + isRead;
        return result;
    }
}
