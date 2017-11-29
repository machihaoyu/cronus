package com.fjs.cronus.dto.api;

import com.fjs.framework.exception.ResponseError;

/**
 * Created by Administrator on 2017/7/20 0020.
 */
public class FegionResponseErrorDTO extends ResponseError {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
