package jp.tokyo.leon.zeus.common.api;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author leon
 * @date 2024/4/4 00:24
 */
@Data
public class ResponseResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int FAIL = -1;

    private int code;

    private String msg;

    private T data;

    public static <T> ResponseResult<T> ok() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> ResponseResult<T> ok(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> ResponseResult<T> ok(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> ResponseResult<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> ResponseResult<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> ResponseResult<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> ResponseResult<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> ResponseResult<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> ResponseResult<T> restResult(T data, int code, String msg) {
        ResponseResult<T> apiResult = new ResponseResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
