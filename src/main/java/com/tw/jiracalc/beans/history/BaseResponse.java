package com.tw.jiracalc.beans.history;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
@Data
public class BaseResponse<T> implements Serializable {
    private Integer statusCode;
    private Map<String, String> headers;
    private T body;

    @Override
    public String toString() {
        return "{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
