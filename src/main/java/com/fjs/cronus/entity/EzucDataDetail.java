package com.fjs.cronus.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ezuc_data_detail")
public class EzucDataDetail {

    /**
     CREATE TABLE `ezuc_data_detail` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `created` datetime NOT NULL COMMENT '创建时间',
     `createid` int(11) DEFAULT NULL COMMENT '创建人',
     `updated` datetime DEFAULT NULL COMMENT '修改时间',
     `updateid` int(11) DEFAULT NULL COMMENT '修改人',
     `deleted` datetime DEFAULT NULL COMMENT '删除时间',
     `deleteid` int(11) DEFAULT NULL COMMENT '删除人',
     `status` int(2) DEFAULT '1' COMMENT '数据状态；0-删除，1-正常',
     `callerDispName` varchar(100) DEFAULT NULL COMMENT 'ezuc字段，业务员名',
     `callerDeptName` varchar(100) DEFAULT NULL COMMENT 'ezuc字段，部门名称',
     `callerAccount`  varchar(100) DEFAULT NULL COMMENT 'ezuc字段，发话者登入账号',
     `callerDbid` int(11) DEFAULT NULL COMMENT 'ezuc字段，发话者数据库Id',
     `callerDeptId` int(100) DEFAULT NULL COMMENT 'ezuc字段，部门系统Id',
     `duration` int(11) DEFAULT NULL COMMENT 'ezuc字段，通话时长;单位秒',
     `totalDuration` int(11) DEFAULT NULL COMMENT 'ezuc字段，拨打时长',
     `startTime` int(11) DEFAULT NULL COMMENT 'ezuc字段，开始时间；Timestamp格式，unix时间',
     `endTime` int(11) DEFAULT NULL COMMENT 'ezuc字段，结束时间；Timestamp格式，unix时间',
     `answerTime` int(11) DEFAULT NULL COMMENT 'ezuc字段，接听时间；Timestamp格式，unix时间',
     `calleeExt` int(11) DEFAULT NULL COMMENT 'ezuc字段，顾客手机号',
     `data` text NOT NULL COMMENT 'ezuc,该条记录原始数据',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='EZUC响应数据明细表';
     */

    private Integer id;
    private Date created;
    private Integer createid;
    private Date updated;
    private Integer updateid;
    private Date deleted;
    private Integer deleteid;
    private Integer status;

    @Column(name = "callerDispName") // 一坑；tk.mybatis工具会将大写的字段加下划线，例如：callerDispName ---> caller_disp_name
    private String callerDispName;
    @Column(name = "callerDeptName")
    private String callerDeptName;
    @Column(name = "callerAccount")
    private String callerAccount;
    @Column(name = "callerDbid")
    private Integer callerDbid;
    @Column(name = "callerDeptId")
    private Integer callerDeptId;
    private Long duration;
    @Column(name = "totalDuration")
    private Long totalDuration;
    @Column(name = "startTime")
    private Long startTime;
    @Column(name = "endTime")
    private Integer endTime;
    @Column(name = "answerTime")
    private Integer answerTime;
    @Column(name = "calleeExt")
    private String calleeExt;
    private String data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCreateid() {
        return createid;
    }

    public void setCreateid(Integer createid) {
        this.createid = createid;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getUpdateid() {
        return updateid;
    }

    public void setUpdateid(Integer updateid) {
        this.updateid = updateid;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public Integer getDeleteid() {
        return deleteid;
    }

    public void setDeleteid(Integer deleteid) {
        this.deleteid = deleteid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCallerDispName() {
        return callerDispName;
    }

    public void setCallerDispName(String callerDispName) {
        this.callerDispName = callerDispName;
    }

    public String getCallerDeptName() {
        return callerDeptName;
    }

    public void setCallerDeptName(String callerDeptName) {
        this.callerDeptName = callerDeptName;
    }

    public String getCallerAccount() {
        return callerAccount;
    }

    public void setCallerAccount(String callerAccount) {
        this.callerAccount = callerAccount;
    }

    public Integer getCallerDbid() {
        return callerDbid;
    }

    public void setCallerDbid(Integer callerDbid) {
        this.callerDbid = callerDbid;
    }

    public Integer getCallerDeptId() {
        return callerDeptId;
    }

    public void setCallerDeptId(Integer callerDeptId) {
        this.callerDeptId = callerDeptId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Integer answerTime) {
        this.answerTime = answerTime;
    }

    public String getCalleeExt() {
        return calleeExt;
    }

    public void setCalleeExt(String calleeExt) {
        this.calleeExt = calleeExt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
