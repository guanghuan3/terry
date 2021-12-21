package com.terry.archer.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果工具类
 */
public class ResponseUtil {

    public static final String RESULT_CODE = "code";
    public static final String RESULT_MESSAGE = "message";
    public static final String RESULT_DATA = "data";

    public static final String RESULT_MESSAGE_SUCCESS = "Success";
    public static final String RESULT_MESSAGE_FAILED = "Failed";
    public static final String RESULT_MESSAGE_ERROR = "System error.";

    public static final String RESULT_CODE_SUCCESS = "0";
    public static final String RESULT_CODE_FAILED = "9998";
    public static final String RESULT_CODE_ERROR = "9999";

    public static Map<String, Object> handleSuccessResult() {
        return handleSuccessResult(null);
    }

    public static Map<String, Object> handleSuccessResult(Object data) {
        Map<String, Object> result = new HashMap<>();

        result.put(RESULT_CODE, RESULT_CODE_SUCCESS);
        result.put(RESULT_MESSAGE, RESULT_MESSAGE_SUCCESS);
        if (CommonUtil.isNotEmpty(data)) {
            result.put(RESULT_DATA, data);
        }
        return result;
    }

    public static Map<String, Object> handleFailedResult() {
        return handleFailedResult(RESULT_MESSAGE_FAILED);
    }

    public static Map<String, Object> handleFailedResult(String message) {
        return handleResult(RESULT_CODE_FAILED, message);
    }

    public static Map<String, Object> handleErrorResult() {
        return handleFailedResult(RESULT_MESSAGE_ERROR);
    }

    public static Map<String, Object> handleErrorResult(String message) {
        return handleResult(RESULT_CODE_ERROR, message);
    }

    private static Map<String, Object> handleResult(String code, String message) {
        Map<String, Object> result = new HashMap<>();

        if (CommonUtil.isNotEmpty(code)) {
            result.put(RESULT_CODE, code);
        } else {
            result.put(RESULT_CODE, RESULT_CODE_FAILED);
        }
        if (CommonUtil.isNotEmpty(message)) {
            result.put(RESULT_MESSAGE, message);
        } else {
            result.put(RESULT_MESSAGE, RESULT_MESSAGE_FAILED);
        }

        return result;
    }

}
