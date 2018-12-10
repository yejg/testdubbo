package com.yejg.testdubbo.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJsonStr(Object object) throws Exception {
        return mapper.writeValueAsString(object);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    public static <T> List<T> toArray(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(json, new TypeReference<List<T>>() {
        });
    }

    public static void main(String[] args) throws Exception {
        String jsonArry = "[{\"userId\":\"001\"},\"sdfs\"]";
        System.out.println(JsonUtil.toObject(jsonArry, List.class));
    }
}
