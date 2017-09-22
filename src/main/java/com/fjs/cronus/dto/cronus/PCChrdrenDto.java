package com.fjs.cronus.dto.cronus;
import java.io.Serializable;

/**
 * Created by msi on 2017/8/17.
 */
public class PCChrdrenDTO<T> implements Serializable {
    private static final long serialVersionUID = 1546456112568212151L;

    private  String  value;
    private  String label;

   /* public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }*/

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
}
