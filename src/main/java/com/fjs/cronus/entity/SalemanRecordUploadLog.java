package com.fjs.cronus.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 业务员通话录音上传记录表.
 */
@Table(name = "saleman_record_upload_log")
public class SalemanRecordUploadLog {

    /**

     CREATE TABLE `saleman_record_upload_log`(
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `created` datetime NOT NULL COMMENT '数据创建时间',
     `createid` int(11) DEFAULT NULL COMMENT '数据创建人',
     `updated` datetime NOT NULL COMMENT '数据修改时间',
     `updateid` int(11) DEFAULT NULL COMMENT '数据修改人',
     `deleted` datetime DEFAULT NULL COMMENT '数据删除时间',
     `deleteid` int(11) DEFAULT NULL COMMENT '数据删除人',
     `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '数据状态：0-删除,1-正常 ',
     `salesman_call_data_id` int(11) NOT NULL COMMENT 'salesman_call_data表id',
     `url` varchar(200) NOT NULL COMMENT '音频文件，阿里OSS地址',
     PRIMARY KEY (`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='业务员通话录音上传记录表';

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
    @Column(name = "salesman_call_data_id")
    private Long salesmanCallDataId;

    private String url;

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

    public Long getSalesmanCallDataId() {
        return salesmanCallDataId;
    }

    public void setSalesmanCallDataId(Long salesmanCallDataId) {
        this.salesmanCallDataId = salesmanCallDataId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
