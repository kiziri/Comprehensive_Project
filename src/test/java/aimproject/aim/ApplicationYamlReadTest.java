package aimproject.aim;


import aimproject.aim.config.ApplicationYmlRead;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationYamlReadTest {
    @Value("${image.path}") // depth가 존재하는 값은 .으로 구분해서 값을 매핑
    private String path;
    @Autowired
    ApplicationYmlRead applicationYmlRead;
    @Test
    void valueAnnotationTest() {
        System.out.println(this.path);
        System.out.println(applicationYmlRead.getPath());
    }
}
