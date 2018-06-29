package com.fjs.cronus.entity;

import javax.persistence.Table;
import java.util.Date;

@Table(name="ezuc_qurtz_log")
public class EzucQurtzLog {

    /**
     CREATE TABLE `ezuc_qurtz_log` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `created` datetime NOT NULL COMMENT '创建时间',
     `createid` int(11) DEFAULT NULL COMMENT '创建人',
     `updated` datetime DEFAULT NULL COMMENT '修改时间',
     `updateid` int(11) DEFAULT NULL COMMENT '修改人',
     `deleted` datetime DEFAULT NULL COMMENT '删除时间',
     `deleteid` int(11) DEFAULT NULL COMMENT '删除人',
     `status` int(2) DEFAULT '1' COMMENT '数据状态；0-删除，1-正常',
     `runinfo` text NULL COMMENT '操作的信息，便于排错，例如异常',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='ezuc 触发定时日志表';
     */

    private Integer id;
    private Date created;
    private Integer createid;
    private Date updated;
    private Integer updateid;
    private Date deleted;
    private Integer deleteid;
    private Integer status;
    private String runinfo;

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

    public String getRuninfo() {
        return runinfo;
    }

    public void setRuninfo(String runinfo) {
        this.runinfo = runinfo;
    }
}
