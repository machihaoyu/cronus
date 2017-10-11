package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/8/17.
 */
public class ThreePcLinkAgeDTO<T> implements Serializable {
    private static final long serialVersionUID = 1546456112578212154L;

    private  String value;
    private  String label;
    private  T children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getChildren() {
        return children;
    }

    public void setChildren(T children) {
        this.children = children;
    }
}
