package com.fjs.cronus.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 业务员通话信息表.
 */
@Table(name = "salesman_call_data")
public class SalesmanCallData {

    /**
     *
     CREATE TABLE `salesman_call_data` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `created` datetime NOT NULL COMMENT '创建时间',
     `createid` int(11) DEFAULT NULL COMMENT '创建人',
     `updated` datetime DEFAULT NULL COMMENT '修改时间',
     `updateid` int(11) DEFAULT NULL COMMENT '修改人',
     `deleted` datetime DEFAULT NULL COMMENT '删除时间',
     `deleteid` int(11) DEFAULT NULL COMMENT '删除人',
     `status` int(2) DEFAULT '1' COMMENT '数据状态；0-删除，1-正常',
     `sales_man_id` int(11) DEFAULT NULL COMMENT '业务员id',
     `sales_man_name` varchar(50) DEFAULT NULL COMMENT '业务员名',
     `sub_companyid` int(11) DEFAULT NULL COMMENT '一级吧',
     `customerid` int(11) DEFAULT NULL COMMENT '顾客id',
     `customer_phone_num` varchar(20) DEFAULT NULL COMMENT '顾客手机号/座机号',
     `start_time` int(20) DEFAULT NULL COMMENT '开始时间；Timestamp格式，unix时间',
     `answer_time` int(20) DEFAULT NULL COMMENT '接通时间；Timestamp格式，unix时间',
     `end_time` int(20) DEFAULT NULL COMMENT '结束时间；Timestamp格式，unix时间',
     `duration` int(20) DEFAULT NULL COMMENT '通话时长，秒',
     `total_duration` int(20) DEFAULT NULL COMMENT '拨打时长，秒',
     `call_type` int(2) DEFAULT NULL COMMENT '通话类型：0-成功',
     `recording_url` text DEFAULT NULL COMMENT '录音url地址',
     `systype` int(2) DEFAULT NULL COMMENT '系统：0-EZUC，1-b端Android',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='业务员通话信息表';

     */

    // 公共字段
    private Long id;
    private Date created;
    private Long createid;
    private Date updated;
    private Long updateid;
    private Date deleted;
    private Long deleteid;
    private Integer status;

    // =======================

    @Column(name = "sales_man_id")
    private Long salesManId;
    @Column(name = "sales_man_name")
    private String salesManName;
    @Column(name = "sub_companyid")
    private Long subCompanyid;
    private Long customerid;
    @Column(name = "customer_phone_num")
    private String customerPhoneNum;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "answer_time")
    private Long answerTime;
    @Column(name = "end_time")
    private Long endTime;
    private Long duration;
    @Column(name = "total_duration")
    private Long totalDuration;
    @Column(name = "call_type")
    private Integer callType;
    @Column(name = "recording_url")
    private String recordingUrl;
    private Integer systype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getUpdateid() {
        return updateid;
    }

    public void setUpdateid(Long updateid) {
        this.updateid = updateid;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public Long getDeleteid() {
        return deleteid;
    }

    public void setDeleteid(Long deleteid) {
        this.deleteid = deleteid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSalesManId() {
        return salesManId;
    }

    public void setSalesManId(Long salesManId) {
        this.salesManId = salesManId;
    }

    public String getSalesManName() {
        return salesManName;
    }

    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    public Long getSubCompanyid() {
        return subCompanyid;
    }

    public void setSubCompanyid(Long subCompanyid) {
        this.subCompanyid = subCompanyid;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    public void setCustomerPhoneNum(String customerPhoneNum) {
        this.customerPhoneNum = customerPhoneNum;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Long answerTime) {
        this.answerTime = answerTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public Integer getCallType() {
        return callType;
    }

    public void setCallType(Integer callType) {
        this.callType = callType;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }

    public Integer getSystype() {
        return systype;
    }

    public void setSystype(Integer systype) {
        this.systype = systype;
    }
}
