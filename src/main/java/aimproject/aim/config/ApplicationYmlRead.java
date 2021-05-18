package aimproject.aim.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.yml",factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "image")
@Getter
@Setter
public class ApplicationYmlRead {
    //@Value("${image.path}") // depth가 존재하는 값은 .으로 구분해서 값을 매핑
    private String path;
}