package com.mwx.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Hojin
 * @date 2022/11/1 18:06
 */
@Data
@ConfigurationProperties(prefix = "minio")//读取配置文件中以minio为前缀的 配置 并通过set get方法赋值
public class MinioProperties {
    /**
     * endpoint: http://192.168.235.128:9000
     * access-key: enjoy6288
     * secret-key: enjoy6288
     * bucket-name: shop
     */
    private String endPoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
