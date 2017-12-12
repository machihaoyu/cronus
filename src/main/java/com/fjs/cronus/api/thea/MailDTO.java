package com.fjs.cronus.api.thea;

import java.util.Date;

/**
 * 消息
 */
public class MailDTO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer fromId;

    private String fromName;

    private Integer toId;

    private String content;

    private Integer status;

    private Date noticeTime;

    private Integer isNotice;

    private Date createTime;

    private Date lastUpdateTime;

    private Integer createUser;

    private Integer lastUpdateUser;

    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Integer getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(Integer isNotice) {
        this.isNotice = isNotice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(Integer lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", fromName='" + fromName + '\'' +
                ", toId=" + toId +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", noticeTime=" + noticeTime +
                ", isNotice=" + isNotice +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createUser=" + createUser +
                ", lastUpdateUser='" + lastUpdateUser + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
