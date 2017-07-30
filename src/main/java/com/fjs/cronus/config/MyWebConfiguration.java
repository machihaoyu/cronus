package com.fjs.cronus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjs.cronus.config.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by Administrator on 2017/7/30 0030.
 */
@Configuration
public class MyWebConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new JsonMapper();
        jsonConverter.setObjectMapper(mapper);
        return jsonConverter;
    }

}
