package com.fjs.cronus.model;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 日志
 * Created by feng on 2017/9/20.
 */
@Table(name = "allocate_log")
public class AllocateLog {

    private static final long serialVersionUID = 1L;

            /*`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
            `operation` varchar(200) DEFAULT NULL COMMENT '操作(自动分配,手动分配,领取客户,查看号码)',
            `customer_id` int(10) NOT NULL DEFAULT '0' COMMENT '客户id',
            `loan_id` int(10) NOT NULL DEFAULT '0' COMMENT '客户id',
            `old_owner_id` int(10) NOT NULL DEFAULT '0' COMMENT '客户原负责人',
            `new_owner_id` int(10) NOT NULL DEFAULT '0' COMMENT '新分配负责人',
            `create_user_id` int(10) NOT NULL DEFAULT '0' COMMENT '分配人id',
            `create_user_name` varchar(100) DEFAULT NULL COMMENT '分配人',
            `create_time` int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
            `return` text COMMENT '结果',*/

    private Integer id;

    private String operation;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "loan_id")
    private Integer loanId;

    @Column(name = "old_owner_id")
    private Integer oldOwnerId;

    @Column(name = "new_owner_id")
    private Integer newOwnerId;

    @Column(name = "create_user_id")
    private Integer createUserId;

    @Column(name = "create_user_name")
    private String createUserName;

    @Column(name = "create_time")
    private Date createTime;

    private String result;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(Integer oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public Integer getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(Integer newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
