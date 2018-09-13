package com.yuanpeng.h5.common;

import java.io.Serializable;

/**
 * Created by  on 2017/11/29.
 * <p>
 * 返回前端实体类
 */
public class RestDto<T> implements Serializable {
    private final static String SUCCESS = "SUCCESS";

    private T result;

    private int code;

    private String message;


    public RestDto() {
    }

    public RestDto(T result, int code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;

    }

    public static <T> RestDto<T> resOk(T result) {
        return resOk(result, SUCCESS);
    }

    public static <T> RestDto<T> resOk(T result, String msg) {

        RestDto<T> dto = new RestDto<T>();
        dto.setCode(0);
        dto.setMessage(msg);
        dto.setResult(result);
        return dto;
    }

    public static <T> RestDto<T> resFail(String errMsg) {

        return resFail(1, errMsg);
    }

    public static <T> RestDto<T> resFail(int code, String errMsg) {

        RestDto<T> dto = new RestDto<T>();
        dto.setCode(code);
        dto.setMessage(errMsg);
        return dto;
    }


    public static String getSUCCESS() {
        return SUCCESS;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
