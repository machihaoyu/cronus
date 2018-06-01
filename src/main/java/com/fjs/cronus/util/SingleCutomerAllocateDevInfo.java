package com.fjs.cronus.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;

import java.util.HashSet;
import java.util.Set;

/**
 * 单个顾客分配时，收集相关信息.
 */
public class SingleCutomerAllocateDevInfo {

    private boolean success = true;

    private JSONObject info = new JSONObject();

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public JSONObject getInfo() {
        return info;
    }

    public void setInfo(String description) {
        this.info.putIfAbsent(description, "--TAG--");
    }

    public void setInfo4Req(String description, ImmutableMap<String, Object> request) {
        this.info.putIfAbsent(description + "-参数", request);
    }

    public void setInfo4Rep(String description, ImmutableMap<String, Object> response) {
        this.info.putIfAbsent(description + "-结果", response);
    }

    public void setInfo(String description, ImmutableMap<String, Object> request, ImmutableMap<String, Object> response) {
        JSONObject temp = new JSONObject();
        temp.putIfAbsent("参数", request);
        temp.putIfAbsent("结果", response);
        this.info.putIfAbsent(description, temp);
    }
}
