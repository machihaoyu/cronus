package com.fjs.cronus.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by chenjie on 2017/12/19.
 */
public class AttachmentModel {

    @ApiModelProperty(name = "id", value = "附件类型id")
    private Integer id;

    @ApiModelProperty(name = "documentName", value = "附件名称")
    private String documentName;

    @ApiModelProperty(name = "picture", value = "附件图片")
    private String picture;

    @ApiModelProperty(name = "documentId", value = "文档id")
    private String documentId;

    @ApiModelProperty(name = "confirm", value = "是否确认：0--待确认;1--已确认")
    private Integer confirm = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    /**
     * 克隆
     * @param attachmentModel
     * @return
     */
    public static AttachmentModel copy(AttachmentModel attachmentModel){
        String jsonString = JSONObject.toJSONString(attachmentModel);
        AttachmentModel object = JSONObject.parseObject(jsonString, AttachmentModel.class);//通过中转，实现新的引用
        return object;
    }

}
