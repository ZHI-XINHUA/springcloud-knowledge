package com.zxh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-29 14:53
 * @Version 1.0
 **/
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String say(){
        return "hi,I an zuul";
    }
}
