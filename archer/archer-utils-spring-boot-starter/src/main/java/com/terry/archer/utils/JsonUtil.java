package com.terry.archer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator
 * on 2019/8/11.
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String objectToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Serialize object to json string error. Object is : {}", new Object[]{obj});
            return "";
        }
    }

    public  static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Deserialize json to object error. Json string is {} ", new Object[]{json});
            return null;
        }
    }

    public static Map<String, Object> jsonToMap(String jsonContent) {
        try {
            return mapper.readValue(jsonContent, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Deserialize json to object error. Json string is {} ", new Object[]{jsonContent});
            return null;
        }
    }
}
