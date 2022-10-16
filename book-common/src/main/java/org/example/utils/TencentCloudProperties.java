package org.example.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:tencentcloud.properties") // 找resource下的配置文件
@ConfigurationProperties(prefix = "tencent.cloud") // 可以使用点,快速操作
public class TencentCloudProperties {
    private String secretId;
    private String secretKey;
}
