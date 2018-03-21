package com.fjs.cronus.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class FastJsonUtils {

    /**
     * 功能描述：把JSON数据转换成普通字符串列表
     *
     * @param jsonData
     *            JSON数据
     * @return
     * @throws Exception
     * @author myclover
     */
    public static List<String> getStringList(String jsonData){
        return JSON.parseArray(jsonData, String.class);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象
     *
     * @param jsonData
     *            JSON数据
     * @param clazz
     *            指定的java对象
     * @return
     * @throws Exception
     * @author myclover
     */
    public static <T> T getSingleBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     *
     * @param jsonData
     *            JSON数据
     * @param clazz
     *            指定的java对象
     * @return
     * @throws Exception
     * @author myclover
     */
    public static <T> List<T> getBeanList(String jsonData, Class<T> clazz) {
        try {
            return JSON.parseArray(jsonData, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的java对象列表
     *
     * @param jsonData
     *            JSON数据
     * @return
     * @throws Exception
     * @author myclover
     */
    public static List<Map<String, Object>> getBeanMapList(String jsonData)
            throws Exception {
        return JSON.parseObject(jsonData,
                new TypeReference<List<Map<String, Object>>>() {
                });
    }
    @SuppressWarnings("unchecked")
    public static Map<String,Object> json2Map(String json){
        return JSON.parseObject(json, Map.class);
    }

    public static String obj2JsonString(Object obj){
        return JSON.toJSONString(obj);
    }

    public static JSONArray stringToJsonArray(String str){

        return  JSONArray.parseArray(str);
    }
}
