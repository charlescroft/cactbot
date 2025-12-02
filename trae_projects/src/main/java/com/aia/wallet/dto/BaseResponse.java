package com.aia.wallet.dto;

public class BaseResponse<T> {
    private int code;
    private String message;
    private T result;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> BaseResponse<T> success(T result) {
        return new BaseResponse<>(200, "Success", result);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null);
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
