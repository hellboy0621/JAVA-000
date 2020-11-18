package com.transformers.spring.boot.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * Spring boot properties configuration.
 */
@Data
@ConfigurationProperties(prefix = "spring.school")
public class SpringBootPropertiesConfiguration {
    private Properties props = new Properties();
}
