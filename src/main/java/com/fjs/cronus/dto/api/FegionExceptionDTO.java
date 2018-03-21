package com.fjs.cronus.dto.api;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20 0020.
 */
public class FegionExceptionDTO implements Serializable {
    private static final long serialVersionUID = 7784163051134497949L;

    private String error;
    private String error_description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
