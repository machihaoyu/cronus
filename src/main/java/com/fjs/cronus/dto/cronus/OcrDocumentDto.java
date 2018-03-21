package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/9/25.
 */
public class OcrDocumentDto  implements Serializable{

    private Integer document_id;//documenId

    private String document_name;

    private String document_c_name;

    private String document_c_name_header;

    private Integer rc_document_id;

    private String documentSavename;

    private String documentSavepath;

    private String Url;

    private Integer flag;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
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

    public Integer getDocument_id() {
        return document_id;
    }

    public void setDocument_id(Integer document_id) {
        this.document_id = document_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getDocument_c_name() {
        return document_c_name;
    }

    public void setDocument_c_name(String document_c_name) {
        this.document_c_name = document_c_name;
    }

    public String getDocument_c_name_header() {
        return document_c_name_header;
    }

    public void setDocument_c_name_header(String document_c_name_header) {
        this.document_c_name_header = document_c_name_header;
    }

    public Integer getRc_document_id() {
        return rc_document_id;
    }

    public void setRc_document_id(Integer rc_document_id) {
        this.rc_document_id = rc_document_id;
    }
}
