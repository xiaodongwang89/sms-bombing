package com.galaxyf.sms.utils.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright(c) 2017 by WayLau.
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author WayLau
 * @version <类版本>
 * @Package: com.galaxyf.common.utils.json
 * @date 2017/12/28  下午3:54
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */
@Slf4j
public class JSONUtil {
    public static <T> T genListBeanByJson(String jsonString, Class<T> containerType, Class<?> objType) {
        T bean = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(containerType, objType);
        try {
            bean = objectMapper.readValue(jsonString, javaType);
        } catch (JsonParseException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genListBeanByJson", jsonString, "JSON 解析异常. "), e);
        } catch (JsonMappingException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genListBeanByJson", jsonString, "JSON 解析映射实体异常. "), e);
        } catch (IOException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genListBeanByJson", jsonString, "IO EXCEPTION"), e);
        }
        return bean;
    }

    public static <T> T genMapBeanByJson(String jsonString, Class<T> containerType, Class<?> keyType,
            Class<?> valueType) {
        T bean = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(containerType, keyType, valueType);
        try {
            bean = objectMapper.readValue(jsonString, javaType);
        } catch (JsonParseException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genMapBeanByJson", jsonString, "JSON 解析异常. "), e);
        } catch (JsonMappingException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genMapBeanByJson", jsonString, "JSON 解析映射实体异常. "), e);
        } catch (IOException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genMapBeanByJson", jsonString, "IO EXCEPTION"), e);
        }
        return bean;
    }

    public static <T> T genBeanByJson(String jsonString, Class<T> claz) {
        T bean = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        try {
            bean = objectMapper.readValue(jsonString, claz);
        } catch (JsonParseException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genBeanByJson", jsonString, "JSON 解析异常. "), e);
        } catch (JsonMappingException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genBeanByJson", jsonString, "JSON 解析映射实体异常. "), e);
        } catch (IOException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genBeanByJson", jsonString, "IO EXCEPTION"), e);
        }
        return bean;
    }

    public static <T> T genBeanByJson(byte[] jsonBytes, Class<T> claz) {
        T bean = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            bean = objectMapper.readValue(jsonBytes, claz);
        } catch (JsonParseException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genBeanByJson", new String(jsonBytes), "JSON 解析异常. "), e);
        } catch (JsonMappingException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genBeanByJson", new String(jsonBytes), "JSON 解析映射实体异常. "), e);
        } catch (IOException e) {
            log.error(String.format("[%s]:: args<%s>, msg<%s>", "genBeanByJson", new String(jsonBytes), "IO EXCEPTION"), e);
        }
        return bean;
    }

    @SuppressWarnings("deprecation")
    public static String dumpObject(Object obj) {
        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        JsonGenerator jsonGenerator = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
            jsonGenerator.writeObject(obj);
            result = out.toString("UTF-8");
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("对象转换字符串异常", e);
        }
        return result;
    }
}
