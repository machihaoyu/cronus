package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/10/20.
 */
public class ThreeCategoryDto implements Serializable {

    private String name;
    private DocumentCategoryDTO  value;
    private String parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentCategoryDTO getValue() {
        return value;
    }

    public void setValue(DocumentCategoryDTO value) {
        this.value = value;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
