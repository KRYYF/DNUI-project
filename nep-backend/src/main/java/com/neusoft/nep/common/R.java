package com.neusoft.nep.common;

import lombok.Data;

/**
 * 统一 API 返回结构
 */
@Data
public class R {

    private Integer code;
    private String msg;
    private Object data;

    private R() {
    }

    private R(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功（无数据）
     */
    public static R success() {
        return new R(200, "ok", null);
    }

    /**
     * 成功（带数据）
     */
    public static R success(Object data) {
        return new R(200, "ok", data);
    }

    /**
     * 成功（自定义消息 + 数据）
     */
    public static R success(String msg, Object data) {
        return new R(200, msg, data);
    }

    /**
     * 失败（默认 500）
     */
    public static R error(String msg) {
        return new R(500, msg, null);
    }

    /**
     * 失败（自定义错误码）
     */
    public static R error(Integer code, String msg) {
        return new R(code, msg, null);
    }
}
