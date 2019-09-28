package com.zxh.user.controller;

import com.zxh.user.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-27 9:46
 * @Version 1.0
 **/
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/say")
    public String sayHi(@RequestParam String name){
        return "Hello "+name;
    }

    @GetMapping("/say1")
    public String sayHi(@RequestHeader String name,@RequestHeader int age){
        return "Hello "+name+" I'm"+age+" years old";
    }

    @PostMapping("/say2")
    public String sayHi(@RequestBody User user){
        return "Hello "+user.getUsername()+" My email number is"+user.getEmail();
    }
}
