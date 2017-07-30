package com.fjs.cronus.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by Administrator on 2017/7/30 0030.
 */
public class JsonMapper extends ObjectMapper {

    public JsonMapper() {
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(
            new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator jgen,
                                      SerializerProvider provider) throws IOException,
                        JsonProcessingException {
                    jgen.writeString("");
                }
            });
    }

}
