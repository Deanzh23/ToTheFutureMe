package com.dean.tothefutureme.letter.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dean.android.framework.convenient.database.annotation.Column;
import com.dean.android.framework.convenient.database.annotation.PrimaryKey;
import com.dean.tothefutureme.BR;
import com.dean.tothefutureme.utils.DateTimeUtils;

import java.io.Serializable;

/**
 * 信件Model
 * <p>
 * Created by dean on 2017/12/6.
 */
public class LetterModel extends BaseObservable implements Serializable {

    /**
     * 信件类型：文字
     */
    public static final int LEETER_TYPE_TEXT = 0;
    /**
     * 信件类型：语音
     */
    public static final int LEETER_TYPE_VOICE = 1;

    /**
     * 信件ID
     */
    @PrimaryKey
    private String letterId;
    /**
     * 收件人ID -> username
     */
    @PrimaryKey
    private String userId;
    /**
     * 信件类型（0：文字；1：语音）
     */
    @Column
    private int type = LEETER_TYPE_TEXT;
    /**
     * 发件人ID -> username
     */
    @Column
    private String senderUserId;
    /**
     * 发件人昵称
     */
    @Column
    private String senderUserNickName;
    /**
     * 发件人头像URL
     */
    @Column
    private String senderAvatarUrl;
    /**
     * 发件日期时间毫秒值
     */
    @Column
    private long sendDateTime;
    /**
     * 收件日期时间毫秒值
     */
    @Column
    private long receiveDateTime;
    /**
     * 本地保存日期时间毫秒值
     */
    @Column
    private long localSaveDateTime;
    /**
     * 本地保存日期时间表示
     */
    private String localSaveDateTimeName;
    /**
     * 文字长度限制
     */
    @Column
    private int letterLengthLimit = 100;
    /**
     * 信件内容
     */
    @Column
    private String content;
    /**
     * 是否是为上传到服务器的本地信件
     */
    @Column
    private boolean isLocal = false;
    /**
     * 是否已读
     * <p>
     * 0：未读
     * 1：已读
     */
    @Column
    private int isRead = 0;

    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String letterId) {
        this.letterId = letterId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    @Bindable
    public String getSenderUserNickName() {
        return senderUserNickName;
    }

    public void setSenderUserNickName(String senderUserNickName) {
        this.senderUserNickName = senderUserNickName;
        notifyPropertyChanged(BR.senderUserNickName);
    }

    @Bindable
    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
        notifyPropertyChanged(BR.senderAvatarUrl);
    }

    @Bindable
    public long getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(long sendDateTime) {
        this.sendDateTime = sendDateTime;
        notifyPropertyChanged(BR.sendDateTime);
    }

    @Bindable
    public long getReceiveDateTime() {
        return receiveDateTime;
    }

    public void setReceiveDateTime(long receiveDateTime) {
        this.receiveDateTime = receiveDateTime;
        notifyPropertyChanged(BR.receiveDateTime);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLetterLengthLimit() {
        return letterLengthLimit;
    }

    public void setLetterLengthLimit(int letterLengthLimit) {
        this.letterLengthLimit = letterLengthLimit;
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public long getLocalSaveDateTime() {
        return localSaveDateTime;
    }

    public void setLocalSaveDateTime(long localSaveDateTime) {
        this.localSaveDateTime = localSaveDateTime;

        setLocalSaveDateTimeName(DateTimeUtils.getDateTimeMillisecond2String(localSaveDateTime));
    }

    public String getLocalSaveDateTimeName() {
        return localSaveDateTimeName;
    }

    public void setLocalSaveDateTimeName(String localSaveDateTimeName) {
        this.localSaveDateTimeName = localSaveDateTimeName;
    }

    @Bindable
    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
        notifyPropertyChanged(BR.isRead);
    }
}
