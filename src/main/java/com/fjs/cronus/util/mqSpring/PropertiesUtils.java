package com.fjs.cronus.util.mqSpring;

import java.util.Properties;

/**
 * Created by feng on 2017/10/18.
 */
public class PropertiesUtils {


    public static Properties initProperties(String producerId, String accessKey, String secretKey) {

        Properties properties = new Properties();
        properties.setProperty("ProducerId", producerId);
        properties.setProperty("AccessKey", accessKey);
        properties.setProperty("SecretKey", secretKey);
        return properties;
    }

}
