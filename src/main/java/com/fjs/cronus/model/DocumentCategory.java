package com.fjs.cronus.model;

import java.util.Date;

public class DocumentCategory extends BaseModel {
    private Integer id;

    private Integer documentCParentId;

    private Boolean documentCLevel;

    private Boolean documentCLevelId;

    private String documentCNameHeader;

    private String documentCName;

    private Integer sort;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDocumentCParentId() {
        return documentCParentId;
    }

    public void setDocumentCParentId(Integer documentCParentId) {
        this.documentCParentId = documentCParentId;
    }

    public Boolean getDocumentCLevel() {
        return documentCLevel;
    }

    public void setDocumentCLevel(Boolean documentCLevel) {
        this.documentCLevel = documentCLevel;
    }

    public Boolean getDocumentCLevelId() {
        return documentCLevelId;
    }

    public void setDocumentCLevelId(Boolean documentCLevelId) {
        this.documentCLevelId = documentCLevelId;
    }

    public String getDocumentCNameHeader() {
        return documentCNameHeader;
    }

    public void setDocumentCNameHeader(String documentCNameHeader) {
        this.documentCNameHeader = documentCNameHeader;
    }

    public String getDocumentCName() {
        return documentCName;
    }

    public void setDocumentCName(String documentCName) {
        this.documentCName = documentCName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}