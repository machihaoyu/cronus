package com.fjs.cronus.dto.App;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2018/1/4.
 */
public class ThreeCategoryDTO implements Serializable {

    private Integer id;

    private Integer documentCParentId;

    private String documentCName;

    private List<ThreeCateDTO> childDto;


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

    public String getDocumentCName() {
        return documentCName;
    }

    public void setDocumentCName(String documentCName) {
        this.documentCName = documentCName;
    }

    public List<ThreeCateDTO> getChildDto() {
        return childDto;
    }

    public void setChildDto(List<ThreeCateDTO> childDto) {
        this.childDto = childDto;
    }
}
