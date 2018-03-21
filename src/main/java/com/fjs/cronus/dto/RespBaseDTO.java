package com.fjs.cronus.dto;

public class RespBaseDTO<T> {

    private int code = 0;//默认0，成功； 非0异常
    private String msg = null;

    private T data;

    /**
     * 返回成功对象(无返回值)
     */
    public RespBaseDTO(){}

    /**
     * 返回成功对象(有返回值)
     * @param t
     */
    public RespBaseDTO(T t) {
        this.data = t;
    }

    /**
     * 异常消息对象
     * @param code
     * @param msg
     */
    public RespBaseDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
