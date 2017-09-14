package com.fjs.cronus.model;

import java.util.Date;

public class Document extends BaseModel {
    private Integer id;

    private String documentName;

    private String documentType;

    private Integer documentSize;

    private String documentExt;

    private String documentMd5;

    private String documentSavename;

    private String documentSavepath;

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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Integer getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(Integer documentSize) {
        this.documentSize = documentSize;
    }

    public String getDocumentExt() {
        return documentExt;
    }

    public void setDocumentExt(String documentExt) {
        this.documentExt = documentExt;
    }

    public String getDocumentMd5() {
        return documentMd5;
    }

    public void setDocumentMd5(String documentMd5) {
        this.documentMd5 = documentMd5;
    }

    public String getDocumentSavename() {
        return documentSavename;
    }

    public void setDocumentSavename(String documentSavename) {
        this.documentSavename = documentSavename;
    }

    public String getDocumentSavepath() {
        return documentSavepath;
    }

    public void setDocumentSavepath(String documentSavepath) {
        this.documentSavepath = documentSavepath;
    }
}