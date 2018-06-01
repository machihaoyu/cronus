package com.fjs.cronus.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 单个顾客分配时，收集相关信息.
 */
public class SingleCutomerAllocateDevInfo {

    private boolean success = true;

    private JSONArray info = new JSONArray();

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public JSONArray getInfo() {
        return info;
    }

    public void setInfo(String description) {
        this.info.add(description);
    }

    public void setInfo4Req(String description, ImmutableMap<String, Object> request) {
        Map<String, ImmutableMap> temp = new HashMap<>();
        temp.put(description + "-参数", request);
        this.info.add(temp);
    }

    public void setInfo4Rep(String description, ImmutableMap<String, Object> response) {
        Map<String, ImmutableMap> temp = new HashMap<>();
        temp.put(description + "-结果", response);
        this.info.add(temp);
    }

    public void setInfo(String description, ImmutableMap<String, Object> request, ImmutableMap<String, Object> response) {

        Map<String, ImmutableMap> temp = new HashMap<>();
        temp.put(description + "-参数", request);
        temp.put(description + "-结果", response);
        this.info.add(temp);
    }
}
