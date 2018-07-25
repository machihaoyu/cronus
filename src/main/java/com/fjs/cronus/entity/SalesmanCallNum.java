package com.fjs.cronus.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "salesman_call_num")
public class SalesmanCallNum {

    /**

     # 通话次数表
     CREATE TABLE `salesman_call_num` (
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
     `sub_companyid` int(11) DEFAULT NULL COMMENT '一级吧id',
     `time` datetime DEFAULT NULL COMMENT '数据所属时间；以天为单位',
     `num` int(20) DEFAULT NULL COMMENT '通话次数',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='通话次数表';

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
    private Date time;
    private Integer num;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
