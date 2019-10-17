package cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ConfigClientController
 * @Description config client 测试
 * @Author xh.zhi
 * @Date 2019-10-12 10:57
 * @Version 1.0
 **/
@RestController
public class ConfigClientController {

    @Value("${profile}")
    private String profile;

    @GetMapping("/profile")
    public String hello(){
        return profile;
    }
}
